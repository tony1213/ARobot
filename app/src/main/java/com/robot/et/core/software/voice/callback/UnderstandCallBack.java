package com.robot.et.core.software.voice.callback;


import com.robot.et.core.software.voice.voiceenum.SceneServiceEnum;

/**
 * Created by houdeming on 2016/10/28.
 */
public interface UnderstandCallBack {
    /**
     * 返回的理解结果
     *
     * @param serviceEnum      所在的场景
     * @param understandResult 理解的结果内容
     */
    void onUnderstandResult(SceneServiceEnum serviceEnum, String understandResult);
}
