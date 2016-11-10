package com.robot.et.core.software.voice.impl.ifly.util;

/**
 * Created by houdeming on 2016/10/29.
 */
public class SpeakConfig {
    // 科大讯飞的appid
    public final static String SPEECH_APPID = "570e1085";

    // 云端发音人：楠楠
    public static String MEN_SPEAK_CLOUD_NANNAN = "nannan";
    // 本地发音人：小燕
    public static String MEN_SPEAK_LOCAL_XIAOYAN = "xiaoyan";
    // 说话的人
    public static String defaultSpeakMen = MEN_SPEAK_CLOUD_NANNAN;


    // 普通话
    public static String MEN_LISTEN_MANDARIN = "mandarin";
    // 粤语
    public static String MEN_LISTEN_CANTONESE = "cantonese";
    // 河南话
    public static String MEN_LISTEN_HENANESE = "henanese";
    // 英语
    public static String MEN_LISTEN_EN_US = "en_us";
    // 听的人
    public static String defaultListenMen = MEN_LISTEN_MANDARIN;


    /**
     * 设置发音人
     * @param speakMen 发音人
     */
    public static void setSpeakMen(String speakMen) {
        defaultSpeakMen = speakMen;
    }

    /**
     * 设置听的人
     * @param listenMen 听的人
     */
    public static void setListenMen(String listenMen) {
        defaultListenMen = listenMen;
    }
}
