package com.robot.et.core.software.push;

import com.robot.et.core.software.push.ali.ALiMsgReceiver;

/**
 * Created by houdeming on 2016/11/17.
 * 推送
 */

public class PushFactory {

    private static final String TAG = "alipush";

    /**
     * 设置阿里推送的alias值
     *
     * @return
     */
    public static void setPushParameter(String content) {
        ALiMsgReceiver.setAlias(content);
    }

    /**
     * 设置推送的回调接口
     * @param iPush
     */
    public static void setPushCallBack(IPush iPush) {
        ALiMsgReceiver.setCallBack(iPush);
    }
}
