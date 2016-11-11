package com.robot.et.business.control;

import android.text.TextUtils;

import com.robot.et.business.view.ViewManager;
import com.robot.et.business.voice.VoiceHandler;
import com.robot.et.core.software.control.emotion.Emotion;
import com.robot.et.core.software.control.emotion.EmotionEnum;

/**
 * Created by houdeming on 2016/11/10.
 * 表情指令
 */

public class EmotionOrder {
    /**
     * 是否是表情
     * @param result
     * @return
     */
    public boolean isMatchEmotion(String result) {
        EmotionEnum emotionEnum = Emotion.getEmotionEnum(result);
        if (emotionEnum != null) {
            int emotionKey = emotionEnum.getEmotionKey();
            ViewManager.getViewCallBack().onShowEmotion(true, emotionKey);
            String answer = emotionEnum.getRequireAnswer();
            if(TextUtils.isEmpty(answer)) {
                VoiceHandler.listen();
            } else {
                VoiceHandler.speakEndToListen(answer);
            }
            return true;
        }
        return false;
    }
}
