package com.robot.et.core.software.voice.callback;

import org.json.JSONObject;

/**
 * Created by houdeming on 2016/7/25.
 * 解析科大讯飞获取当前的service
 */
public interface ParseResultCallBack {
    /**解析结果回调
     *
     * @param question 问题
     * @param service 场景的标志
     * @param jObject json对象
     */
    void getResult(String question, String service, JSONObject jObject);
    /**解析异常
     *
     * @param errorMsg 异常信息
     */
    void onError(String errorMsg);
}
