package com.robot.et.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.robot.et.R;
import com.robot.et.business.voice.VoiceService;
import com.robot.et.lib.business.entity.User;
import com.robot.et.lib.core.http.HttpTaskHelper;
import com.robot.et.test.fresco.FrescoActivity;
import com.robot.et.test.picasso.PicassoActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        startService(new Intent(this, VoiceService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, VoiceService.class));
    }

    @OnClick(R.id.button)
    public void goJNIMethod(){
        Log.i(TAG,"exec goJNIMethod");
//        Intent intent = new Intent();
//        intent.setClass(this, JNIActivity.class);
//        startActivity(intent);
//        SpeechImpl.getInstance().startListen(new ListenCallBack() {
//            @Override
//            public void onListenResult(String result) {
//                VoiceResultHandler.handVoiceResult(MainActivity.this, result);
//            }
//        });


//        VideoPhone.callPhone(this, CallType.CALL_TYPE_LOOK, "123", true);
    }
    @OnClick(R.id.button2)
    public void goRetrofitMethod(){
        Log.i(TAG,"exec goRetrofitMethod");
        HttpTaskHelper.gitHubAPI(this)
                .userInfoString("tony1213")
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.e(TAG,"onResponse:"+response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG,"onFailure:"+t.toString());
                    }
                });
    }

    @OnClick(R.id.button3)
    public void goNormalGet(){
        Call<User> userCall = HttpTaskHelper.gitHubAPI(this)
                .userInfo("tony1213");
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User body = response.body();
                Log.d(TAG,body == null ? "onResponse:"+"body==null":"onResponse:"+body.toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (call.isCanceled()){
                    Log.d(TAG,"the call is called,"+toString());
                }else {
                    Log.e(TAG,t.toString());
                }
            }
        });
    }
    @OnClick(R.id.button4)
    public void rxGet() {
        HttpTaskHelper.gitHubAPI(this)
                .userInfoRx("tony1213")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onNext(User user) {
                        Log.d(TAG, "onNext："+user.toString());
                    }
                });
    }
    @OnClick(R.id.button5)
    public void testUmengError(){
        int i = 100;
        int j = 0;
        int z = i/j;
    }
    @OnClick(R.id.button6)
    public void testFresco(){
        Intent intent = new Intent();
        intent.setClass(this, FrescoActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.button7)
    public void testPicasso(){
        Intent intent = new Intent();
        intent.setClass(this, PicassoActivity.class);
        startActivity(intent);
    }
}
