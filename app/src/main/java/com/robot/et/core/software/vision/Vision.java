package com.robot.et.core.software.vision;

import android.graphics.Bitmap;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.robot.et.VisionManager;
import com.robot.et.core.software.vision.callback.BodyPositionCallBack;
import com.robot.et.core.software.vision.callback.LearnOpenCallBack;
import com.robot.et.core.software.vision.callback.LearnWaringCallBack;
import com.robot.et.core.software.vision.callback.VisionBitmapCallBack;
import com.robot.et.core.software.vision.callback.VisionImgInfoCallBack;
import com.robot.et.core.software.vision.callback.VisionInitCallBack;
import com.robot.et.core.software.vision.callback.VisionLearnCallBack;
import com.robot.et.core.software.vision.callback.VisionRecogniseCallBack;
import com.robot.et.callback.VisionCallBack;

/**
 * Created by houdeming on 2016/11/14.
 * 视觉的处理
 * 备注：当视觉初始化成功后，底层便有图像上传，不管中间开关学习与跟随，图像还是存在，只有
 * 反初始化视觉后，图像不再回调，
 */

public class Vision implements VisionCallBack {

    private static final String TAG = "visionHand";
    private static Vision visionHandle;
    private VisionManager visionManager;
    private VisionLearnCallBack learnCallBack;
    private VisionRecogniseCallBack recogniseCallBack;
    private BodyPositionCallBack positionCallBack;
    private LearnOpenCallBack learnOpenCallBack;
    private LearnWaringCallBack learnWaringCallBack;
    private VisionImgInfoCallBack imgInfoCallBack;
    private VisionBitmapCallBack bitmapCallBack;
    private Bitmap mBitmap;

    private Vision() {
        visionManager = new VisionManager(this);
    }

    public static Vision getInstance() {
        if (visionHandle == null) {
            synchronized (Vision.class) {
                if (visionHandle == null) {
                    visionHandle = new Vision();
                }
            }
        }
        return visionHandle;
    }

    /**
     * 初始化视觉
     */
    public void initVision(VisionInitCallBack callBack) {
        try {
            int visionId = visionManager.visionInit();
            Log.i(TAG, "visionId==" + visionId);
            if (callBack != null) {
                callBack.onVisionInitResult(visionId == 0 ? true : false);
            }
        } catch (RemoteException e) {
            Log.i(TAG, "initVision() RemoteException");
        }
    }

    /**
     * 视觉反初始化
     */
    public void unInitVision() {
        try {
            visionManager.visionUninit();
        } catch (RemoteException e) {
            Log.i(TAG, "unInitVision() RemoteException");
        }
    }

    /**
     * 打开学习
     */
    public void openLearn(LearnOpenCallBack callBack) {
        learnOpenCallBack = callBack;
        try {
            visionManager.visionLearnOpen();
        } catch (RemoteException e) {
            Log.i(TAG, "openLearn() RemoteException");
        }
    }

    /**
     * 关闭学习
     */
    public void closeLearn() {
        try {
            visionManager.visionLearnClose();
        } catch (RemoteException e) {
            Log.i(TAG, "closeLearn() RemoteException");
        }
    }

    /**
     * 回调视觉中的提示（在 learnOpenEnd() 方法回调后调用 ）
     *
     * @param callBack
     */
    public void getLearnWaring(LearnWaringCallBack callBack) {
        learnWaringCallBack = callBack;
    }

    /**
     * 开始学习
     *
     * @param content
     */
    public void startLearn(String content, VisionLearnCallBack callBack) {
        learnCallBack = callBack;
        try {
            visionManager.objLearnStartLearn(content);
        } catch (RemoteException e) {
            Log.i(TAG, "startLearn() RemoteException");
        }
    }

    /**
     * 开始识别
     */
    public void startRecognise(VisionRecogniseCallBack callBack) {
        recogniseCallBack = callBack;
        try {
            visionManager.objLearnStartRecog();
        } catch (RemoteException e) {
            Log.i(TAG, "startRecognise() RemoteException");
        }
    }

    /**
     * 打开人体检测
     */
    public void openBodyDetect() {
        try {
            visionManager.bodyDetectOpen();
        } catch (RemoteException e) {
            Log.i(TAG, "openBodyDetect() RemoteException");
        }
    }

    /**
     * 关闭人体检测
     */
    public void closeBodyDetect() {
        try {
            visionManager.bodyDetectClose();
        } catch (RemoteException e) {
            Log.i(TAG, "closeBodyDetect() RemoteException");
        }
    }

    /**
     * 人体检测
     */
    public void getBodyPosition(BodyPositionCallBack callBack) {
        positionCallBack = callBack;
        try {
            visionManager.bodyDetectGetPos();
        } catch (RemoteException e) {
            Log.i(TAG, "getBodyPosition() RemoteException");
        }
    }

    /**
     * 获取视觉的信息
     *
     * @param visionImgInfoCallBack
     */
    public void getImgInfo(VisionImgInfoCallBack visionImgInfoCallBack) {
        imgInfoCallBack = visionImgInfoCallBack;
        try {
            visionManager.getVisionImgInfo();
        } catch (RemoteException e) {
            Log.i(TAG, "getImgInfo() RemoteException");
        }
    }

    /**
     * 获取视觉的bitmap (获取图像时先下发bitmap 到底层)
     *
     * @param visionBitmapCallBack
     */
    public void getVisionBitmap(VisionBitmapCallBack visionBitmapCallBack) {
        bitmapCallBack = visionBitmapCallBack;
        try {
            visionManager.getVisionImgBitmap();
        } catch (RemoteException e) {
            Log.i(TAG, "getVisionBitmap() RemoteException");
        }
    }

    /**
     * 设置一张图片到底层用于回调视觉的bitmap
     *
     * @param visionImgWidth  视觉图片的宽
     * @param visionImgHeight 视觉图片的高
     */
    public void sendBitmapToFramework(int visionImgWidth, int visionImgHeight) {
        mBitmap = Bitmap.createBitmap(visionImgWidth, visionImgHeight, Bitmap.Config.ARGB_8888);
        if (mBitmap != null) {
            visionManager.setImgBitmap(mBitmap);
        }
    }

    /**
     * 底层回调的方法
     */
    @Override
    public void learnOpenEnd() {
        Log.i(TAG, "learnOpenEnd()");
        if (learnOpenCallBack != null) {
            learnOpenCallBack.onLearnOpenEnd();
        }
    }

    /**
     * 底层回调的方法
     */
    @Override
    public void learnWaring(int id) {
        Log.i(TAG, "learnWaring() id==" + id);
        String content = "";
        switch (id) {
            case 0:
                content = "物体已经在最佳位置";
                break;
            case 1:
                content = "距离太近了";
                break;
            case 2:
                content = "距离太远了";
                break;
            case 10:
                content = "物体太低了";
                break;
            case 11:
                content = "物体太高了";
                break;
            case 12:
                content = "物体太靠左了";
                break;
            case 13:
                content = "物体太靠右了";
                break;
            case 20:
                content = "没看见有东西";
                break;
            case 21:
                content = "东西太小了，看不清";
                break;
        }
        if (learnWaringCallBack != null) {
            learnWaringCallBack.onLearnWaring(id, content);
        }
    }

    /**
     * 底层回调的方法
     */
    @Override
    public void learnEnd() {
        Log.i(TAG, "learnEnd()");
        if (learnCallBack != null) {
            learnCallBack.onLearnEnd();
        }
    }

    /**
     * 底层回调的方法
     */
    @Override
    public void learnRecogniseEnd(String name, int conf) {
        Log.i(TAG, "learnRecogniseEnd() name==" + name + "---conf==" + conf);
        String content = "";
        boolean isRecogniseSuccess = false;
        if (TextUtils.isEmpty(name)) {// 沒有识别成功
            content = "我不认识这个东西，让我学习一下吧！";
        } else {// 识别成功
            isRecogniseSuccess = true;
            switch (conf) {
                case 1:// 可能（40%以上）
                    content = "这好像是：" + name;
                    break;
                case 2:// 是（80%以上）
                    content = "这是" + name;
                    break;
                case 3:// 一定（95%以上）
                    content = "这是" + name;
                    break;
                case 10:// 不是就是的情况
                    if (name.contains("|")) {
                        String[] data = name.split("\\|");
                        Log.i(TAG, "learnRecogniseEnd() data[0]==" + data[0] + "---data[1]==" + data[1]);
                        content = "这个不是：" + data[0] + "，就是：" + data[1];
                    } else {
                        isRecogniseSuccess = false;
                        content = "我不认识这个东西，让我学习一下吧！";
                    }
                    break;
            }
        }
        if (recogniseCallBack != null) {
            recogniseCallBack.onVisionRecogniseResult(isRecogniseSuccess, content);
        }
    }

    /**
     * 底层回调的方法
     */
    @Override
    public void bodyPosition(float centerX, float centerY, float centerZ) {
        Log.i(TAG, "learnOpenEnd() centerX==" + centerX + "--centerY==" + centerY + "--centerZ==" + centerZ);
        if (positionCallBack != null) {
            positionCallBack.onBodyPosition(centerX, centerY, centerZ);
        }
    }

    /**
     * 底层回调的方法
     */
    @Override
    public void getVisionImgInfo(int width, int height, int dataType) {
        Log.i(TAG, "getVisionImgInfo() width==" + width + "--height==" + height + "--dataType==" + dataType);
        if (imgInfoCallBack != null) {
            imgInfoCallBack.onVisionImgInfo(width, height, dataType);
        }

    }

    /**
     * 底层回调的方法
     */
    @Override
    public void getImgBitmap(Bitmap bitmap) {
        if (bitmapCallBack != null) {
            bitmapCallBack.onVisionBitmap(bitmap);
        }
    }
}
