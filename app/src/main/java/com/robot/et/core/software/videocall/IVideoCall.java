package com.robot.et.core.software.videocall;

/**
 * Created by houdeming on 2016/10/30.
 * 对外拨打电话接口
 */
public interface IVideoCall {
    /**呼叫电话
     *
     * @param callType 呼叫类型
     * @param roomNum 房间号
     * @param isCallByVoice 是否是语音呼叫
     */
    void callPhone(int callType, String roomNum, boolean isCallByVoice);

    //  电话是否正在进行
    boolean isPhoneCallIng();

    // 关闭电话
    void closePhone();
}
