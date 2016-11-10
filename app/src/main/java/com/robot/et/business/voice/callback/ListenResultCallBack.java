package com.robot.et.business.voice.callback;

/**
 * Created by houdeming on 2016/11/9.
 * 语音听写的结果回调
 */
public interface ListenResultCallBack {
    /**
     * 听写的结果
     * @param result 听写内容
     */
    void onListenResult(String result);
}
