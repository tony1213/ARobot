package com.robot.et.core.software.videocall;

import com.robot.et.core.software.videocall.callback.PhoneCallBack;

/**
 * Created by houdeming on 2016/10/30.
 * 对外拨打电话接口
 */
public interface IVideoCall {
    /**呼叫电话
     *
     * @param callType 呼叫类型
     * @param roomNum 房间号
     */
    void callPhone(int callType, String roomNum, PhoneCallBack callBack);

    //  电话是否正在进行
    boolean isPhoneCallIng();

    // 关闭电话
    void closePhone();
}
