package com.robot.et.core.software.push;

/**
 * Created by houdeming on 2016/11/16.
 * 推送的接口
 */

public interface IPush {
    /**
     * 获取推送结果
     * @param result 结果
     */
    void onPushResult(String result);
}
