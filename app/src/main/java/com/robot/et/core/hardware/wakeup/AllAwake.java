package com.robot.et.core.hardware.wakeup;

/**
 * 一直都会唤醒
 */
public class AllAwake {

    static {
        System.loadLibrary("AllAwake");
    }

    /**
     * 初始化
     * @return
     */
    public static native int initAllAwake();

    /**
     * 获取唤醒的值
     * @return
     */
    public static native int getAllAwakeValue();

    /**
     * 设置参数
     * @param devId 设备的ID
     * @param para 参数值
     * @return
     */
    public static native int setAllAwakePara(int devId, int para);
}
