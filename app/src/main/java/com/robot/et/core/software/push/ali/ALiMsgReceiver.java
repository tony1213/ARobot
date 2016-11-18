package com.robot.et.core.software.push.ali;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.robot.et.core.software.push.IPush;

import java.util.Map;

/**
 * Created by houdeming on 2016/9/14.
 * 用于接收推送的通知和消息
 */
public class ALiMsgReceiver extends MessageReceiver {

    private static final String TAG = "alipush";
    private static IPush mCallBack;

    /**
     * 设置阿里推送的alias值
     *
     * @return
     */
    public static void setAlias(String content) {
        Log.i(TAG, "content===" + content);
        PushServiceFactory.getCloudPushService().addAlias(content, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.i(TAG, "添加别名成功");
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.i(TAG, "添加别名失败，errorCode: " + errorCode + ", errorMessage：" + errorMessage);
            }
        });
    }

    public static void setCallBack(IPush iPush) {
        mCallBack = iPush;
    }

    /**
     * 推送通知的回调方法
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        if (null != extraMap) {
            for (Map.Entry<String, String> entry : extraMap.entrySet()) {
                Log.i(TAG, "Get diy param : Key=" + entry.getKey() + " , Value=" + entry.getValue());
            }
        } else {
            Log.i(TAG, "收到通知 && 自定义消息为空");
        }
        Log.i(TAG, "收到一条推送通知 ： " + title);
    }

    /**
     * 推送消息的回调方法
     * 接受推送消息
     *
     * @param context
     * @param cPushMessage
     */
    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        try {
            String result = cPushMessage.getContent();
            Log.i(TAG, "收到一条推送消息cPushMessage.getContent()===" + result);
            if (!TextUtils.isEmpty(result)) {
                if (mCallBack != null) {
                    mCallBack.onPushResult(result);
                }
            }

        } catch (Exception e) {
            Log.i(TAG, "收到推送消息失败Exception==" + e.getMessage());
        }
    }

    /**
     * 从通知栏打开通知的扩展处理
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        CloudPushService cloudPushService = PushServiceFactory.getCloudPushService();
//        cloudPushService.setNotificationSoundFilePath();
        Log.i(TAG, "onNotificationOpened ： " + " : " + title + " : " + summary + " : " + extraMap);
    }


    @Override
    public void onNotificationRemoved(Context context, String messageId) {
        Log.i(TAG, "onNotificationRemoved ： " + messageId);
    }


    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Log.i(TAG, "onNotificationClickedWithNoAction ： " + " : " + title + " : " + summary + " : " + extraMap);
    }
}