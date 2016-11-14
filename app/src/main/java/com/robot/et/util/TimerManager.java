package com.robot.et.util;

import java.util.Timer;

/**
 * Timer计时器管理
 */
public class TimerManager {

    /**
     * 创建计时器
     *
     * @return
     */
    public static Timer createTimer() {
        return new Timer();
    }

    /**
     * 取消计时
     *
     * @param timer
     */
    public static void cancelTimer(Timer timer) {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }
}
