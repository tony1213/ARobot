package com.robot.et.core.software.voice.impl.turing;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.robot.et.core.software.voice.IVoice;
import com.robot.et.core.software.voice.callback.ListenCallBack;
import com.robot.et.core.software.voice.callback.SpeakCallBack;
import com.robot.et.core.software.voice.callback.UnderstandCallBack;
import com.turing.androidsdk.InitListener;
import com.turing.androidsdk.SDKInit;
import com.turing.androidsdk.SDKInitBuilder;
import com.turing.androidsdk.TuringApiManager;

import org.json.JSONException;
import org.json.JSONObject;

import turing.os.http.core.ErrorMessage;
import turing.os.http.core.HttpConnectionListener;
import turing.os.http.core.RequestResult;

/**
 * Created by houdeming on 2016/9/3.
 * 图灵
 */
public class TuringVoice implements IVoice {
    private final String TAG = "voiceT";
    // 图灵的appid
    private final String TURING_APPID = "8314e713b83b80dbe26264214907bce1";
    // 图灵的secret
    private final String TURING_SECRET = "b4e5061c950ea99a";
    // 图灵的UNIQUEID   填写一个任意的标示，没有具体要求，但一定要写
    private final String TURING_UNIQUEID = "131313131";
    private TuringApiManager mTuringApiManager;
    private Context context;
    private UnderstandCallBack understandCallBack;

    public TuringVoice(Context context) {
        this.context = context;
        initTuringSDK(TURING_SECRET, TURING_APPID, TURING_UNIQUEID);
    }

    // turingSDK初始化
    private void initTuringSDK(final String secret, final String appId, final String uniqueId) {
        SDKInitBuilder builder = new SDKInitBuilder(context).setSecret(secret)
                .setTuringKey(appId).setUniqueId(uniqueId);

        SDKInit.init(builder, new InitListener() {
            @Override
            public void onFail(String error) {
                Log.i(TAG, "图灵error===" + error);
                //异常处理 异常后重新去初始化
                initTuringSDK(secret, appId, uniqueId);
            }

            @Override
            public void onComplete() {
                // 获取userid成功后，才可以请求Turing服务器，需要请求必须在此回调成功，才可正确请求
                mTuringApiManager = new TuringApiManager(context);
                mTuringApiManager.setHttpListener(myHttpConnectionListener);
            }
        });
    }

    // 图灵文本理解监听器
    private HttpConnectionListener myHttpConnectionListener = new HttpConnectionListener() {

        @Override
        public void onSuccess(RequestResult result) {// 理解成功
            String content = "";
            if (result != null) {
                try {
                    JSONObject result_obj = new JSONObject(result.getContent().toString());
                    if (result_obj.has("text")) {
                        content = (String) result_obj.get("text");
                        Log.i(TAG, "图灵content====" + content);
                        if (!TextUtils.isEmpty(content)) {
                            // 对天气的结果特殊处理
                            if (content.contains(":") && content.contains("周") && content.contains("风") && content.contains(";")) {
                                Log.i(TAG, "从科大讯飞没有获取到天气问图灵");
                                content = getWeatherContent(content);
                            }
                        }
                    }
                } catch (JSONException e) {
                    Log.i(TAG, "图灵JSONException====" + e.getMessage());
                }
            }
            if (understandCallBack != null) {
                understandCallBack.onUnderstandResult(null, content);
            }
        }

        @Override
        public void onError(ErrorMessage errorMessage) {// 理解失败
            String msg = errorMessage.getMessage();
            Log.i(TAG, "图灵errorMessage.getMessage()====" + msg);
            if (understandCallBack != null) {
                understandCallBack.onUnderstandResult(null, "");
            }
        }
    };

    @Override
    public void startSpeak(String speakContent, String speakMen, SpeakCallBack callBack) {

    }

    @Override
    public void stopSpeak() {

    }

    @Override
    public void startListen(String listenMen, ListenCallBack callBack) {

    }

    @Override
    public void stopListen() {

    }

    @Override
    public void understanderText(String content, UnderstandCallBack callBack) {
        this.understandCallBack = callBack;
        if (mTuringApiManager == null || TextUtils.isEmpty(content)) {
            if (understandCallBack != null) {
                understandCallBack.onUnderstandResult(null, "");
            }
            return;
        }
        mTuringApiManager.requestTuringAPI(content);
    }

    @Override
    public void destroyVoice() {

    }

    /*
       * 对图灵返回的天气进行处理
       * content weather格式 上海:05/16 周一,15-24° 23° 晴 北风微风; 05/17 周二,16-26° 晴 东南风微风;
       * 05/18 周三,17-26° 多云 东风微风; 05/19 周四,19-26° 多云 东风微风;
       */
    private String getWeatherContent(String content) {
        String result = "";
        if (!TextUtils.isEmpty(content)) {
            String[] datas = content.split(";");
            result = datas[0];
            String[] tempDatas = result.split(",");
            result = tempDatas[1];
            String cityData = tempDatas[0];
            String[] citys = cityData.split("\\:");
            String city = citys[0];
            String[] weathers = result.split(" ");
            // weathers[0]15-24° weathers[1]23° weathers[2]晴 weathers[3]北风微风
            if (weathers != null && weathers.length > 0) {
                StringBuffer buffer = new StringBuffer(1024);
                buffer.append(city).append("市").append("天气：").append(weathers[2]).append(",气温：").append(weathers[0]).append(",风力：").append(weathers[3]);
                result = buffer.toString();
            }
        }
        return result;
    }
}
