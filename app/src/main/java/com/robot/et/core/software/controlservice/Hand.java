package com.robot.et.core.software.controlservice;

/**
 * Created by houdeming on 2016/11/3.
 * 手运动数据的封装
 */
public class Hand {
    // 头标识
    private static final int HEAD_SIGN = 2;
    // 数据长度
    private static final int DATA_LENGTH = 6;
    // 左手
    public static final int HAND_LEFT = 1;
    // 右手
    public static final int HAND_RIGHT = 2;
    // 双手
    public static final int HAND_LEFT_RIGHT = 3;
    // 归位，双手回归到0度
    public static final int HAND_HOMING = 4;
    // 双手同向
    public static final int HAND_DIRECTION_SAME = 1;
    // 双手反向
    public static final int HAND_DIRECTION_DIFFERENT = 2;
    // 双手动作依次执行，左手  动作执行完成后  右手  开始执行动作
    public static final int HAND_DIRECTION_LEFT_RIGHT = 3;
    // 双手动作依次执行，右手  动作执行完成后  左手  开始执行动作
    public static final int HAND_DIRECTION_RIGHT_LEFT = 4;
    // 运动到指定角度固定动作
    public static final int HAND_NUM_FIXED = 255;
    // 一直运动
    public static final int HAND_NUM_AWAYS = 256;

    /**
     * 手运动数据的封装
     * @param handChoose 手的选择
     * @param handDirection 手的方向，（双手执行时此字段有效，其余时默认填成0）
     * @param frontAngle 向前角度 0~120 垂直向下为0度，向前抬最大值为120
     * @param backAngle 向后角度 0~60   垂直向下为0度，向后摆最大值为60
     * @param moveTime 运动时间 单位100ms，填成0时使用底板默认时间
     * @param moveNum 运动次数 除了固定动作以及一直运动之外，发来的次数是多少就转多少
     * @return
     */
    public static byte[] getHandData(int handChoose, int handDirection, int frontAngle, int backAngle, int moveTime, int moveNum) {
        byte[] datas = new byte[8];
        datas[0] = HEAD_SIGN;
        datas[1] = DATA_LENGTH;
        datas[2] = (byte)handChoose;
        datas[3] = (byte)handDirection;
        datas[4] = (byte)frontAngle;
        datas[5] = (byte)backAngle;
        datas[6] = (byte)moveTime;
        datas[7] = (byte)moveNum;
        return datas;
    }
}
