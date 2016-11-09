package com.robot.et.core.software.control.emotion;

import android.text.TextUtils;

/**
 * Created by houdeming on 2016/11/3.
 * 获取表情的资源
 */
public class Emotion {
    /**
     * 获取表情的资源id
     *
     * @param str
     * @return
     */
    public static int getEmotionKey(String str) {
        int emotionKey = 0;
        if (!TextUtils.isEmpty(str)) {
            for (EmotionEnum emotionEnum : EmotionEnum.values()) {
                if (str.contains(emotionEnum.getEmotionName())) {
                    emotionKey = emotionEnum.getEmotionKey();
                }
            }
        }
        return emotionKey;
    }

    /**
     * 获取表情的枚举值
     *
     * @param str
     * @return
     */
    public static EmotionEnum getEmotionEnum(String str) {
        if (!TextUtils.isEmpty(str)) {
            for (EmotionEnum emotionEnum : EmotionEnum.values()) {
                if (str.contains(emotionEnum.getEmotionName())) {
                    return emotionEnum;
                }
            }
        }
        return null;
    }
}
