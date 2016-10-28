package com.robot.et.business.voice;

/**
 * Created by houdeming on 2016/10/28.
 */
public interface ListenCallBack {
    /**
     * 听的结果回调
     *
     * @param result 听写的结果
     */
    void onListenResult(String result);
}
