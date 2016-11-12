package com.robot.et.lib.business.api;

import com.robot.et.lib.business.entity.SmsContent;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Tony on 2016/11/4.
 */

public interface BusinessAPI {
    //短信验证码发送
    @FormUrlEncoded
    @POST("sms/sendMsg")
    Call<SmsContent> getSmsContent(@Field("mobile") String mobile, @Field("templateId") String templateId);
}
