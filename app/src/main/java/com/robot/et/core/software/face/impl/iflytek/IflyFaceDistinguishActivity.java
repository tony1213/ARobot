package com.robot.et.core.software.face.impl.iflytek;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.Process;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.iflytek.cloud.FaceDetector;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.util.Accelerometer;
import com.robot.et.R;
import com.robot.et.core.software.face.callback.FaceCallBack;
import com.robot.et.core.software.face.impl.iflytek.util.FaceParseResult;
import com.robot.et.core.software.face.impl.iflytek.util.FaceRect;
import com.robot.et.core.software.face.impl.iflytek.util.FaceUtil;
import com.robot.et.entity.FaceInfo;
import com.robot.et.util.BitmapUtil;
import com.robot.et.util.FaceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// 人脸识别
public class IflyFaceDistinguishActivity extends Activity {
    private static final String FACE_TAG = "face";
    private SurfaceView mPreviewSurface;
    private SurfaceView mFaceSurface;
    private Camera mCamera;
    // 默认打开后置摄像头  前置摄像头：CAMERA_FACING_FRONT  后置摄像头：CAMERA_FACING_BACK
    private int mCameraId = CameraInfo.CAMERA_FACING_BACK;
    // Camera nv21格式预览帧的尺寸，默认设置640*480
    private int PREVIEW_WIDTH = 640;
    private int PREVIEW_HEIGHT = 480;
    // 预览帧数据存储数组和缓存数组
    private byte[] nv21;
    private byte[] buffer;
    //识别到的图片信息
    private byte[] mImageData;
    // 缩放矩阵
    private Matrix mScaleMatrix = new Matrix();
    // 加速度感应器，用于获取手机的朝向
    private Accelerometer mAcc;
    // FaceDetector对象，集成了离线人脸识别：人脸检测、视频流检测功能
    private FaceDetector mFaceDetector;

    private boolean mStopTrack;
    private int isAlign = 0;
    private FaceRequest mFaceRequest;
    private String auId;
    private int testCount;//脸部识别的次数
    private int noFaceCount;//没有检测到人脸的次数
    private int screenCenterX;//屏幕中心X
    private int screenCenterY;//屏幕中心Y
    private boolean isSendAngle;//发送移动角度
    private boolean isVerify;//是否在验证
    public static IflyFaceDistinguishActivity instance;
    private List<FaceInfo> faceInfos = new ArrayList<FaceInfo>();
    private boolean isVoiceOpen;
    private FaceCallBack mCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置屏幕常亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_face_verify);
        // 获取屏幕中心点
        getScreenCenterPoint();
        instance = this;
        // 初始化界面
        initUI();
        nv21 = new byte[PREVIEW_WIDTH * PREVIEW_HEIGHT * 2];
        buffer = new byte[PREVIEW_WIDTH * PREVIEW_HEIGHT * 2];
        mAcc = new Accelerometer(this);
        // 获取FaceDetector对象
        mFaceDetector = FaceDetector.createDetector(this, null);
        mFaceRequest = new FaceRequest(this);
        // 获取已注册人脸的数据
        Intent intent = getIntent();
        faceInfos = intent.getParcelableArrayListExtra("FaceInfo");
        isVoiceOpen = intent.getBooleanExtra("isVoiceOpen", false);
        mCallBack = IflyFaceImpl.getCallBack();

    }

    //获取屏幕中心点坐标
    private void getScreenCenterPoint() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        screenCenterX = dm.widthPixels / 2;
        screenCenterY = dm.heightPixels / 2;
        Log.i(FACE_TAG, "screenCenterX===" + screenCenterX);
        Log.i(FACE_TAG, "screenCenterY===" + screenCenterY);
    }

    // SurfaceView预览监听器
    private Callback mPreviewCallback = new Callback() {

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            closeCamera();
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            openCamera();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mScaleMatrix.setScale(width / (float) PREVIEW_HEIGHT, height / (float) PREVIEW_WIDTH);
        }
    };

    // 初始化界面
    private void initUI() {
        mPreviewSurface = (SurfaceView) findViewById(R.id.sfv_preview);
        mFaceSurface = (SurfaceView) findViewById(R.id.sfv_face);
        mPreviewSurface.getHolder().addCallback(mPreviewCallback);
        mPreviewSurface.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mFaceSurface.setZOrderOnTop(true);
        mFaceSurface.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    // 打开相机
    private void openCamera() {
        if (null != mCamera) {
            return;
        }
        // 检查相机权限
        if (!checkCameraPermission()) {
            Log.i(FACE_TAG, "摄像头权限未打开，请打开后再试");
            mStopTrack = true;
            return;
        }
        // 只有一个摄相头，打开后置
        if (Camera.getNumberOfCameras() == 1) {
            mCameraId = CameraInfo.CAMERA_FACING_BACK;
        }
        try {
            // 打开相机
            mCamera = Camera.open(mCameraId);
            if (CameraInfo.CAMERA_FACING_FRONT == mCameraId) {
                Log.i(FACE_TAG, "前置摄像头已开启，点击可切换");
            } else {
                Log.i(FACE_TAG, "后置摄像头已开启，点击可切换");
            }
            // 设置相机Parameters参数
            Parameters params = mCamera.getParameters();
            params.setPreviewFormat(ImageFormat.NV21);
            params.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
            mCamera.setParameters(params);
            // 设置显示的偏转角度，大部分机器是顺时针90度，某些机器需要按情况设置
//        mCamera.setDisplayOrientation(90);
            mCamera.setPreviewCallback(new PreviewCallback() {

                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    System.arraycopy(data, 0, nv21, 0, data.length);
                }
            });
        } catch (Exception e) {
            Log.i(FACE_TAG, "Camera Exception==" + e.getMessage());
            // 关闭相机
            closeCamera();
            finish();
            if (mCallBack != null) {
                mCallBack.onFaceError();
            }
            return;
        }

        try {
            mCamera.setPreviewDisplay(mPreviewSurface.getHolder());
            mCamera.startPreview();
        } catch (IOException e) {
            Log.i(FACE_TAG, "Camera IOException==" + e.getMessage());
        }
    }

    // 关闭相机
    private void closeCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    // 检查相机权限
    private boolean checkCameraPermission() {
        int status = checkPermission(permission.CAMERA, Process.myPid(), Process.myUid());
        if (PackageManager.PERMISSION_GRANTED == status) {
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 开始人脸检测
        testFace();
    }

    // 开始人脸检测
    private void testFace() {
        if (null != mAcc) {
            mAcc.start();
        }

        mStopTrack = false;
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!mStopTrack) {
                    if (null == nv21) {
                        continue;
                    }
                    synchronized (nv21) {
                        System.arraycopy(nv21, 0, buffer, 0, nv21.length);
                    }
                    // 获取手机朝向，返回值0,1,2,3分别表示0,90,180和270度
                    int direction = Accelerometer.getDirection();
                    boolean frontCamera = (CameraInfo.CAMERA_FACING_FRONT == mCameraId);
                    // 前置摄像头预览显示的是镜像，需要将手机朝向换算成摄相头视角下的朝向。
                    // 转换公式：a' = (360 - a)%360，a为人眼视角下的朝向（单位：角度）
                    if (frontCamera) {
                        // SDK中使用0,1,2,3,4分别表示0,90,180,270和360度
                        direction = (4 - direction) % 4;
                    }

                    if (mFaceDetector == null) {
                        // 离线视频流检测功能需要单独下载支持离线人脸的SDK 请开发者前往语音云官网下载对应SDK
                        Log.i(FACE_TAG, "本SDK不支持离线视频流检测");
                        break;
                    }

                    // 获取人脸数据
                    String result = mFaceDetector.trackNV21(buffer, PREVIEW_WIDTH, PREVIEW_HEIGHT, isAlign, direction);
                    FaceRect[] faces = FaceParseResult.parseResult(result);

                    // 获取检测到人脸的画布
                    Canvas canvas = null;
                    try {
                        canvas = mFaceSurface.getHolder().lockCanvas();
                    } catch (Exception e) {
                        Log.i(FACE_TAG, "canvas Exception==" + e.getMessage());
                    }

                    if (null == canvas) {
                        continue;
                    }
                    // 设置画布
                    canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                    canvas.setMatrix(mScaleMatrix);

                    // 如果没有检测到人脸一直检测，连续检测次数大于250次（大约15秒左右）的时候自动关闭人脸检测
                    if (faces != null) {
                        if (faces.length <= 0) {
                            noFaceCount++;
                            if (noFaceCount >= 250) {
                                mStopTrack = true;
                                noFaceCount = 0;
                                sendMsg("请让我看见你", false);
                            } else {
                                mFaceSurface.getHolder().unlockCanvasAndPost(canvas);
                                continue;
                            }
                        }
                    }

                    if (null != faces && frontCamera == (CameraInfo.CAMERA_FACING_FRONT == mCameraId)) {
                        for (FaceRect face : faces) {
                            face.bound = FaceUtil.RotateDeg90(face.bound, PREVIEW_WIDTH, PREVIEW_HEIGHT);
                            if (face.point != null) {
                                for (int i = 0; i < face.point.length; i++) {
                                    face.point[i] = FaceUtil.RotateDeg90(face.point[i], PREVIEW_WIDTH, PREVIEW_HEIGHT);
                                }
                            }
                            FaceUtil.drawFaceRect(canvas, face, PREVIEW_WIDTH, PREVIEW_HEIGHT, frontCamera, false);
                        }
                        Log.i(FACE_TAG, "faces.length==" + faces.length);
                        // 检测到有多张人脸时，继续检测，多张人脸时不支持注册验证
                        if (faces.length > 1) {
                            Log.i(FACE_TAG, "多张人脸");
                            mFaceSurface.getHolder().unlockCanvasAndPost(canvas);
                            continue;
                        }

                        // 只有1张人脸并且没在验证的时候开始验证人脸
                        if (faces.length == 1 && !isVerify) {
                            Log.i(FACE_TAG, "只有1张人脸");
                            isVerify = true;
                            // 拷贝到临时数据中
                            byte[] tmp = new byte[nv21.length];
                            System.arraycopy(nv21, 0, tmp, 0, nv21.length);

                            mImageData = BitmapUtil.bitmap2Byte(BitmapUtil.decodeToBitMap(tmp, PREVIEW_WIDTH, PREVIEW_HEIGHT, 80));
                            noFaceCount = 0;
                            // 转身  多次检测的时候只转一次头(控制头转)
//                            if (!isSendAngle) {
//                                isSendAngle = true;
//                                FaceRect face = faces[0];
//                                //X中心点
//                                float pointX = FaceUtil.getRectCenterX(face);
//                                Log.i(FACE_TAG, "pointX===" + pointX);
//                                //Y中心点
//                                float pointY = FaceUtil.getRectCenterY(face);
//                                Log.i(FACE_TAG, "pointY===" + pointY);
//                                /* 横向转头：0-180  正中间 90，  向左转90-0   向右 90-180
//                                   越靠近90度的，距离正中间的位置越近
//                                   上下抬头：0-60
//                                   上下以垂直方向为0度，向前10度即-10，向后10度即+10。
//                                   左右横向运动以正中为0度，向右10度即-10，向左10度即+10。
//                                 */
//                                String directionValue = getTurnDigit(pointY);
//                                Log.i(FACE_TAG, "directionValue===" + directionValue);
//                                // 发送控制头部运动的广播
//                            }
                            // 处理识别到的人脸
                            handleFace(mImageData, faceInfos);
                        }
                    } else {
                        Log.i(FACE_TAG, "faces===0");
                    }
                    // 人脸识别结束一定要释放holder对象，不然报异常
                    mFaceSurface.getHolder().unlockCanvasAndPost(canvas);
                }
            }
        }).start();
    }

    //获取头部旋转角度
    private String getTurnDigit(float pointY) {
        String directionValue = "";
        float tempValue;
        String sign = "";
        if (pointY < screenCenterY) {//向左转
            Log.i(FACE_TAG, "向左转");
            sign = "-";
            tempValue = screenCenterY - pointY;
        } else {//向右转
            Log.i(FACE_TAG, "向右转");
            tempValue = pointY - screenCenterY;
        }
        if (tempValue <= 20) {
            directionValue = sign + 5;
        } else if (20 < tempValue && tempValue <= 50) {
            directionValue = sign + 10;
        } else {
            directionValue = sign + 15;
        }
        return directionValue;
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeCamera();
        if (null != mAcc) {
            mAcc.stop();
        }
        mStopTrack = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁对象
        mFaceDetector.destroy();
        instance = null;
    }

    // 脸部识别后的处理 对传来的注册数据一个一个遍历验证人脸，验证不通过便注册人脸
    private void handleFace(byte[] mImageData, List<FaceInfo> faceInfos) {
        if (null != mImageData && mImageData.length > 0) {
            int size = faceInfos.size();
            Log.i(FACE_TAG, "handleFace faceInfos.size()===" + size);
            if (faceInfos != null && size > 0) {
                FaceInfo info = faceInfos.get(0);
                String auId = info.getAuthorId();
                FaceManager.setAuthorName(info.getAuthorName());
                FaceManager.setNewFaceInfo(faceInfos);
                verify(mImageData, auId);
            } else {
                registerFace(mImageData);
            }
        } else {
            Log.i(FACE_TAG, "handleFace mImageData== null");
            isVerify = false;
            isSendAngle = false;
        }
    }

    // 调用sdk方法验证
    private void verify(byte[] mImageData, String auId) {
        if (null != mImageData && mImageData.length > 0) {
            // 设置用户标识，格式为6-18个字符（由字母、数字、下划线组成，不得以数字开头，不能包含空格）。
            // 当不设置时，云端将使用用户设备的设备ID来标识终端用户。
            mFaceRequest.setParameter(SpeechConstant.AUTH_ID, auId);
            mFaceRequest.setParameter(SpeechConstant.WFR_SST, "verify");
            mFaceRequest.sendRequest(mImageData, mRequestListener);
        } else {
            Log.i(FACE_TAG, "verify mImageData== null");
            isVerify = false;
            isSendAngle = false;
        }
    }

    // 调用sdk方法注册
    private void registerFace(byte[] mImageData) {
        if (null != mImageData && mImageData.length > 0) {
            auId = getOnlyTime();
            mFaceRequest.setParameter(SpeechConstant.AUTH_ID, auId);
            mFaceRequest.setParameter(SpeechConstant.WFR_SST, "reg");
            mFaceRequest.sendRequest(mImageData, mRequestListener);
        } else {
            Log.i(FACE_TAG, "registerFace mImageData== null");
            isVerify = false;
            isSendAngle = false;
        }
    }

    // 验证注册监听器
    private RequestListener mRequestListener = new RequestListener() {

        @Override
        public void onEvent(int eventType, Bundle params) {
        }

        // 返回结果
        @Override
        public void onBufferReceived(byte[] buffer) {
            boolean isError = false;
            try {
                String result = new String(buffer, "utf-8");
                Log.i(FACE_TAG, "result===" + result);

                JSONObject object = new JSONObject(result);
                String type = object.optString("sst");
                if ("reg".equals(type)) {// 注册
                    register(object);
                } else if ("verify".equals(type)) {// 验证
                    verify(object);
                }
            } catch (UnsupportedEncodingException e) {
                Log.i(FACE_TAG, "RequestListener  UnsupportedEncodingException");
                isError = true;
            } catch (JSONException e) {
                Log.i(FACE_TAG, "RequestListener  JSONException");
                isError = true;
            } finally {
                if (isError) {
                    isVerify = false;
                    isSendAngle = false;
                }
            }
        }

        // 调用该方法表示图片不对或者模糊看不清
        @Override
        public void onCompleted(SpeechError error) {
            if (error != null) {
                testCount++;
                if (testCount < 5) {
                    isVerify = false;
                    isSendAngle = false;
                } else {
                    sendMsg("您离我太远了，可以靠近一点再试试吗", false);
                }
            }
        }
    };

    // 注册
    private void register(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {
            Log.i(FACE_TAG, "注册失败");
            sendMsg("可以靠近点，让我再认识你一次吗？", false);
            return;
        }
        if ("success".equals(obj.get("rst"))) {
            Log.i(FACE_TAG, "注册成功");
            FaceManager.setAuthorId(auId);
            sendMsg("你好，我叫小雪，请问你叫什么名字呢？", true);
        } else {
            Log.i(FACE_TAG, "注册失败");
            sendMsg("可以靠近点，让我再认识你一次吗？", false);
        }
    }

    // 验证，如果验证失败的话，继续验证下一个
    private void verify(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {
            Log.i(FACE_TAG, "验证失败");
            handleFace(mImageData, FaceManager.getFaceInfos());
            return;
        }
        if ("success".equals(obj.get("rst"))) {
            if (obj.getBoolean("verf")) {
                Log.i(FACE_TAG, "通过验证");
                if (isVoiceOpen) {//语音开启人脸识别
                    sendMsg("你是" + FaceManager.getAuthorName(), true);
                } else {//人体感应开启人脸识别
                    sendMsg("很高兴又见到你，" + FaceManager.getAuthorName() + ",有什么可以帮你", true);
                }
            } else {
                Log.i(FACE_TAG, "验证不通过");
                handleFace(mImageData, FaceManager.getFaceInfos());
            }
        } else {
            Log.i(FACE_TAG, "验证失败");
            handleFace(mImageData, FaceManager.getFaceInfos());
        }
    }

    // 发送说话的广播
    private void sendMsg(String content, boolean isVerifySuccess) {
        finish();
        if (mCallBack != null) {
            mCallBack.onFaceResult(isVerifySuccess, content);
        }
    }

    //获取时间的唯一数
    private String getOnlyTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTime = format.format(date);
        return dateTime;
    }
}
