package com.robot.et.test.http;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.robot.et.R;
import com.robot.et.lib.business.entity.SmsContent;
import com.robot.et.lib.core.http.HttpTaskHelper;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public final String TAG = "http";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_http);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.getsms)
    public void getSMSContent(){
        HttpTaskHelper.gitHubAPI(this)
                .getSmsContent("15021565127","23842")
                .enqueue(new Callback<SmsContent>() {
            @Override
            public void onResponse(Call<SmsContent> call, Response<SmsContent> response) {
                Log.i(TAG,"onResponse:"+response.body().resultCode);
            }

            @Override
            public void onFailure(Call<SmsContent> call, Throwable t) {
                Log.e(TAG,"onFailure");
            }
        });
    }
}
