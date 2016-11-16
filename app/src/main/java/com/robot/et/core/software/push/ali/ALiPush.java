package com.robot.et.core.software.push.ali;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;

/**
 * Created by houdeming on 2016/9/16.
 * 阿里推送
 */
public class ALiPush {

    private static final String TAG = "alipush";

    public ALiPush(Context context, String robotNum) {
        setAlia(robotNum);
    }

    // 设置别名
    private void setAlia(String robotNum) {
        Log.i(TAG, "robotNum===" + robotNum);
        PushServiceFactory.getCloudPushService().addAlias(robotNum, new CommonCallback() {
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

}
