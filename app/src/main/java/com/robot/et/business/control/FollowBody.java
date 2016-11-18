package com.robot.et.business.control;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.robot.et.business.control.orderenum.MoveEnum;
import com.robot.et.business.vision.Vision;
import com.robot.et.business.vision.callback.BodyPositionCallBack;
import com.robot.et.config.GlobalConfig;
import com.robot.et.core.software.slam.SlamtecLoader;
import com.robot.et.util.TimerManager;
import com.slamtec.slamware.robot.Pose;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 跟随
 */

public class FollowBody {

    private static final String TAG = "follow";
    private static final String TAG_ANGLE = "angle";
    private static FollowBody followBody = null;
    private Timer timer;
    // 停止的距离
    private final int STOP_VALUE = 115;
    private float robotX;
    private float robotY;
    private float robotAngle;
    private Pose pose;
    private int posX, posZ;

    private FollowBody() {

    }

    public static FollowBody getInstance() {
        if (followBody == null) {
            synchronized (FollowBody.class) {
                if (followBody == null) {
                    followBody = new FollowBody();
                }
            }
        }
        return followBody;
    }

    /**
     * 跟随
     */
    public void follow() {
        if (!GlobalConfig.isFollow) {
            GlobalConfig.isFollow = true;
            Log.i(TAG, "openBodyDetect()");
            Vision.getInstance().openBodyDetect();
        }
        pose = new Pose();
        timer = TimerManager.createTimer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 0, 30);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        }, 0, 300);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Vision.getInstance().getBodyPosition(new BodyPositionCallBack() {
                        @Override
                        public void onBodyPosition(float centerX, float centerY, float centerZ) {
                            posX = (int) centerX;
                            int posY = (int) centerY;
                            posZ = (int) centerZ;
                            // x 代表左右位置【0-320】  y 代表上下位置【0-240】   z 代表距离人的距离
                            Log.i(TAG, "获取到人体的位置：X＝" + posX + "--Y==" + posY + "--Z==" + posZ);
//                            ViewManager.getViewCallBack().onShowText("vX＝" + posX + "\n" + "vZ=" + posZ + "\n" + "pX=" + getX(posX, posZ) + "\n"
//                                    + "pY=" + getY(posX, posZ) + "\n" + "Yaw=" + getYaw(posX) + "\n" + "rX=" + robotX + "\n" + "rY=" + robotY
//                                    + "\n" + "rG=" + robotAngle);
//                            ViewManager.getViewCallBack().onShowText("vX＝" + posX + "\n" + "vZ=" + posZ);
                        }
                    });
                    break;
                case 1:
//                    Location location = SlamtecLoader.getInstance().getCurrentRobotPose();
//                    Rotation rotation = SlamtecLoader.getInstance().getCurrentRotation();
//                    robotX = location.getX();
//                    robotY = location.getY();
//                    robotAngle = rotation.getYaw();
//                    robotX = (float) (Math.round(robotX * 100)) / 100;
//                    robotY = (float) (Math.round(robotY * 100)) / 100;
//                    robotAngle = (float) (Math.round(robotAngle * 100)) / 100;
//                    Log.i(TAG_ANGLE, "RX=" + robotX + "--RY=" + robotY + "--YAW=" + robotAngle);

                    if (GlobalConfig.isConnectSlam) {
//                        if (posX != 0 && posZ != 0) {
//                            Log.i(TAG_ANGLE, "运动");
////                            getPose(posX, posZ);
////                            SlamtecLoader.getInstance().execSetGoal(getPose(posX, posZ));
//                        } else {
//                            Log.i(TAG, "停止");
////                            SlamtecLoader.getInstance().execBasicMove(MoveEnum.STOP.getMoveKey());
//                        }

                        if (posX != 0 && posZ != 0) {
                            if (!followTurn(posX)) {
                                followMove(posZ);
                            }

                        } else {
                            Log.i(TAG, "停止");
                            SlamtecLoader.getInstance().execBasicMove(MoveEnum.STOP.getMoveKey());
                        }

                    }
                    break;
            }
        }
    };

    // 左转右转 中间是160.在150-170之间默认走直线, <150左转  >170右转
    private boolean followTurn(int posX) {
        // 防止不停的转，要与上一次的位置比较，超过一定范围再转
        if (posX < 130) {
            Log.i(TAG, "左转");
            SlamtecLoader.getInstance().execBasicRotate(getAngle(posX), MoveEnum.LEFT.getMoveKey(), 0);
            return true;
        }

        if (posX > 190) {
            Log.i(TAG, "右转");
            SlamtecLoader.getInstance().execBasicRotate(getAngle(posX), MoveEnum.RIGHT.getMoveKey(), 0);
            return true;
        }
        return false;
    }

    // 走还是停
    private void followMove(int posZ) {
        if (posZ < STOP_VALUE) {
            Log.i(TAG, "后退");
            SlamtecLoader.getInstance().execBasicMove(MoveEnum.BACKWARD.getMoveKey());
        } else if (posZ >= STOP_VALUE && posZ <= STOP_VALUE + 40) {// 在停止的范围内40个差距内就停止
            Log.i(TAG, "停止");
            SlamtecLoader.getInstance().execBasicMove(MoveEnum.STOP.getMoveKey());
        } else {
            Log.i(TAG, "前进");
            SlamtecLoader.getInstance().execBasicMove(MoveEnum.FORWARD.getMoveKey());
        }
    }

    /**
     * 停止跟随
     */
    public void stopFollow() {
        if (GlobalConfig.isFollow) {
            GlobalConfig.isFollow = false;
            Vision.getInstance().closeBodyDetect();
            if (timer != null) {
                TimerManager.cancelTimer(timer);
                timer = null;
            }
        }
    }

    private Pose getPose(int posX, int posZ) {
        pose.setX(getX(posX, posZ));
        pose.setY(getY(posX, posZ));
        pose.setYaw(getYaw(posX));
        return pose;
    }

    private float getYaw(int posX) {
        float YawAngle = posX * 5 / 32 - 25;
//        float radians = (float) Math.PI * YawAngle / 180;
//        radians = (float) (Math.round(radians * 100)) / 100;
//        Log.i(TAG_ANGLE, "radians===" + radians);
        float radians = YawAngle;
        return radians;
    }

    private float getX(int posX, int posZ) {
        double personAngle = posX * 5 / 32 - 25;
//        float X = (float) (posZ * Math.sin(90 - personAngle));
//        X = X / 100;
//        X = (float) (Math.round(X * 100)) / 100;
//        Log.i(TAG_ANGLE, "X / 100===" + X / 100);
        float X = (float) personAngle;
        return X;
    }

    private float getY(int posX, int posZ) {
        double personAngle = posX * 5 / 32 - 25;
//        float Y = (float) (posZ * Math.cos(90 - personAngle));
//        Y = Y / 100;
//        Y = (float) (Math.round(Y * 100)) / 100;
//        Log.i(TAG_ANGLE, "Y / 100===" + Y / 100);
        float Y = (float) personAngle;
        return Y;
    }

    private int getAngle(int posX) {
        return Math.abs(posX * 5 / 32 - 25) - 3;
    }
}
