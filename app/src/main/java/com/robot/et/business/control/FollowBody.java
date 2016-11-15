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

import java.util.Timer;
import java.util.TimerTask;

/**
 * 跟随
 */

public class FollowBody {

    private static final String TAG = "follow";
    private static FollowBody followBody = null;
    private Timer timer;

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
        timer = TimerManager.createTimer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 1000, 500);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Vision.getInstance().getBodyPosition(new BodyPositionCallBack() {
                @Override
                public void onBodyPosition(float centerX, float centerY, float centerZ) {
                    int posX = (int) centerX;
                    int posY = (int) centerY;
                    int posZ = (int) centerZ;
                    // X==171.0--Y==71.0--Z==152.94118
                    Log.i(TAG, "获取到人体的位置：X＝" + posX + "--Y==" + posY + "--Z==" + posZ);
                    if (GlobalConfig.isConnectSlam) {
                        SlamtecLoader.getInstance().execBasicMove(MoveEnum.FORWARD.getMoveKey());
                    }

                }
            });
        }
    };

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
}
