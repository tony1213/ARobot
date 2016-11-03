package com.robot.et.core.software.controlservice;

/**
 * Created by houdeming on 2016/11/3.
 * 头部运动数据的封装
 */
public class Head {
    // 头标识
    private static final int HEAD_SIGN = 1;
    // 数据长度
    private static final int DATA_LENGTH = 5;
    // 上下 上下运动时：0~20 上下以水平方向为0度，最上角度20，最下角度 20
    public static final int HEAD_DIRECTION_UP_DOWN = 1;
    // 左右 左右运动时：0~90 正中间 0 最左边90，最右边90
    public static final int HEAD_DIRECTION_LEFT_RIGHT = 2;
    // 归位 正中间 0
    public static final int HEAD_DIRECTION_HOMING = 3;
    // 运动到指定角度固定动作
    public static final int HEAD_NUM_FIXED = 255;
    // 一直运动
    public static final int HEAD_NUM_AWAYS = 256;

    /**
     * 头部运动数据的封装
     * @param headDirection 方向
     * @param upOrLeftAngle 向上/左角度
     * @param downOrRightAngle 向下/右角度
     * @param turnTime 执行时间 单位100ms，单位100ms，填成0时使用底板默认时间
     * @param turnNum 执行次数 除了固定动作以及一直运动之外，发来的次数是多少就转多少
     * @return
     */
    public static byte[] getHeadData(int headDirection, int upOrLeftAngle, int downOrRightAngle, int turnTime, int turnNum) {
        byte[] datas = new byte[7];
        datas[0] = HEAD_SIGN;
        datas[1] = DATA_LENGTH;
        datas[2] = (byte) headDirection;
        datas[3] = (byte)upOrLeftAngle;
        datas[4] = (byte)downOrRightAngle;
        datas[5] = (byte)turnTime;
        datas[6] = (byte)turnNum;
        return datas;
    }
}
