package com.robot.et.core.software.controlservice;

/**
 * Created by houdeming on 2016/11/3.
 * 呼吸灯数据的封装
 */
public class BLN {
    // 头标识
    private static final int HEAD_SIGN = 3;
    // 数据长度
    private static final int DATA_LENGTH = 3;
    // 打开
    public static final int BLN_STATUS_OPEN = 1;
    // 关闭
    public static final int BLN_STATUS_CLOSE = 2;
    // 闪烁
    public static final int BLN_STATUS_BLINK = 3;
    // 红色
    public static final int BLN_COLOR_RED = 1;
    // 绿色
    public static final int BLN_COLOR_GREEN = 2;
    // 蓝色
    public static final int BLN_COLOR_BLUE = 3;
    // 白色
    public static final int BLN_COLOR_WHITE = 4;

    /**
     * 呼吸灯数据的封装
     *
     * @param blnStatus        呼吸灯状态
     * @param blnColor         呼吸灯颜色
     * @param blnBlinkInterval 呼吸灯呼吸的间隔时间0-255，单位100ms
     * @return
     */
    public static byte[] getBLNData(int blnStatus, int blnColor, int blnBlinkInterval) {
        byte[] datas = new byte[5];
        datas[0] = HEAD_SIGN;
        datas[1] = DATA_LENGTH;
        datas[2] = (byte) blnStatus;
        datas[3] = (byte) blnColor;
        datas[4] = (byte) blnBlinkInterval;
        return datas;
    }
}
