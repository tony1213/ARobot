package com.robot.et.core.software.videocall.callback;

/**
 * Created by houdeming on 2016/11/8.
 */
public interface PhoneCallBack {
    /**
     * 电话正在连接中
     */
    void onPhoneConnectIng();

    /**
     * 电话接通
     */
    void onPhoneConnect();

    /**
     * 电话断开
     */
    void onPhoneDisconnect();

    /**
     * 电话异常
     *
     * @param errorMsg 异常信息
     */
    void onPhoneError(String errorMsg);
}
