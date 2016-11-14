package com.robot.et.business.control;

import com.robot.et.business.vision.Vision;
import com.robot.et.util.TimerManager;

import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.delay;

/**
 * Created by houdeming on 2016/11/14.
 * 跟随
 */

public class FollowBody {

    private boolean isOpenBodyDetect;
    private Timer timer;

    public void follow() {
        if (!isOpenBodyDetect) {
            isOpenBodyDetect = true;
            Vision.getInstance().openBodyDetect();
        }
        timer = TimerManager.createTimer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, delay, 1000);
    }

    public void stopFollow() {

    }
}
