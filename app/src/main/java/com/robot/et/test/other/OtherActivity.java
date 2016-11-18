package com.robot.et.test.other;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.robot.et.R;
import com.robot.et.base.BaseActivity;
import com.robot.et.core.software.camera.CameraFactory;
import com.robot.et.core.software.camera.ICamera;
import com.robot.et.core.software.camera.callback.CameraCallBack;
import com.robot.et.core.software.face.FaceFactory;
import com.robot.et.core.software.face.IFace;
import com.robot.et.core.software.face.callback.FaceCallBack;
import com.robot.et.core.software.music.IMusic;
import com.robot.et.core.software.music.MusicFactory;
import com.robot.et.core.software.music.callback.MusicCallBack;
import com.robot.et.core.software.music.config.MusicConfig;
import com.robot.et.core.software.system.alarm.AlarmRemindManager;
import com.robot.et.core.software.videocall.IVideoCall;
import com.robot.et.core.software.videocall.VideoCallFactory;
import com.robot.et.core.software.videocall.callback.PhoneCallBack;
import com.robot.et.core.software.videocall.config.VideoCallConfig;
import com.robot.et.core.software.videoplay.IVideoPlay;
import com.robot.et.core.software.videoplay.VideoPlayFactory;
import com.robot.et.core.software.videoplay.callback.VideoPlayCallBack;

import java.io.File;

public class OtherActivity extends BaseActivity implements View.OnClickListener {

    private IVideoPlay videoPlay;
    private ICamera systemCamera;
    private IFace face;
    private IMusic local;
    private IMusic ximalaya;
    private IVideoCall videoCall;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        Button systemMusicBtn = (Button) findViewById(R.id.btn_system_music);
        Button xiMaMusicBtn = (Button) findViewById(R.id.btn_xima_music);
        Button xiMaRadioBtn = (Button) findViewById(R.id.btn_ximalaya_radio);
        Button playVideoBtn = (Button) findViewById(R.id.btn_play_video);
        Button stopBtn = (Button) findViewById(R.id.btn_stop);
        Button takePhotoBtn = (Button) findViewById(R.id.btn_take_photo);
        Button alarmBtn = (Button) findViewById(R.id.btn_alarm);
        Button faceBtn = (Button) findViewById(R.id.btn_face);
        Button phoneBtn = (Button) findViewById(R.id.btn_phone);
        Button voiceBtn = (Button) findViewById(R.id.btn_voice);
        systemMusicBtn.setOnClickListener(this);
        xiMaMusicBtn.setOnClickListener(this);
        xiMaRadioBtn.setOnClickListener(this);
        playVideoBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        takePhotoBtn.setOnClickListener(this);
        alarmBtn.setOnClickListener(this);
        faceBtn.setOnClickListener(this);
        phoneBtn.setOnClickListener(this);
        voiceBtn.setOnClickListener(this);

        face = FaceFactory.produceIflyFace(this);
        systemCamera = CameraFactory.produceLocalCamera(this);
        local = MusicFactory.produceLocalPlay(this);
        ximalaya = MusicFactory.produceXiMaLaYaPlay(this);
        videoCall = VideoCallFactory.produceAgora(this);
        videoPlay = VideoPlayFactory.produceLocalPlay(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_system_music:
                playSystem();
//                Music.playMusic(OtherActivity.this, MusicConfig.PLAY_MUSIC, "http://file.kuyinyun.com/group1/M00/38/CB/rBBGdVPT8n-AQ-hzABeFmCKyIMo468.mp3",1);
                break;
            case R.id.btn_xima_music:
                playXima(MusicConfig.PLAY_MUSIC, "青花瓷");
//                Music.playByXiMaLaYa(OtherActivity.this, MusicConfig.PLAY_MUSIC, "青花瓷");
                break;
            case R.id.btn_ximalaya_radio:
                playXima(MusicConfig.PLAY_RADIO, "江西音乐广播");
//                Music.playByXiMaLaYa(OtherActivity.this, MusicConfig.PLAY_RADIO, "江西音乐广播");
                break;
            case R.id.btn_play_video:
                String fileSrc = Environment.getExternalStorageDirectory() + File.separator + "robot" + File.separator + "视频"
                        + File.separator + "熊出没.mp4";
                videoPlay.play(fileSrc, new VideoPlayCallBack() {
                    @Override
                    public void playStart() {
                        Log.i("player", "playStart()");
                    }

                    @Override
                    public void playComplected() {
                        Log.i("player", "playComplected()");
                    }

                    @Override
                    public void playFail() {
                        Log.i("player", "playFail()");
                    }
                });
                break;
            case R.id.btn_stop:
                local.stopPlay();
                ximalaya.stopPlay();
                break;
            case R.id.btn_take_photo:
                systemCamera.takePhoto(new CameraCallBack() {
                    @Override
                    public void onCameraResult(byte[] data) {
                        Log.i("player", "onCameraResult()  data.length===" + data.length);
                    }

                    @Override
                    public void onFail() {
                        Log.i("player", "onFail()");
                    }
                });
                break;
            case R.id.btn_alarm:
                addAlarm();
                break;
            case R.id.btn_face:
                face();
                break;
            case R.id.btn_phone:
                callPone();
                break;
            case R.id.btn_voice:
                intent = new Intent(this, VoiceServiceTest.class);
                startService(intent);
                break;
        }
    }

    private void callPone() {
        videoCall.callPhone(VideoCallConfig.CALL_TYPE_VIDEO, "123", new PhoneCallBack() {
            @Override
            public void onPhoneConnectIng() {
                Log.i("videoPhone", "onPhoneConnectIng()");
            }

            @Override
            public void onPhoneConnect() {
                Log.i("videoPhone", "onPhoneConnect()");
            }

            @Override
            public void onPhoneDisconnect() {
                Log.i("videoPhone", "onPhoneDisconnect()");
            }

            @Override
            public void onPhoneError(String errorMsg) {
                Log.i("videoPhone", "onPhoneError errorMsg==" + errorMsg);
            }
        });
    }

    private void face() {
        face.openFaceDistinguish(null, new FaceCallBack() {

            @Override
            public void onFaceDistinguish(boolean isDistinguishSuccess, String faceName) {
                Log.i("faceImpl", "isDistinguishSuccess==" + isDistinguishSuccess);
                Log.i("faceImpl", "faceName==" + faceName);
            }

            @Override
            public void onFaceRegister(boolean isRegisterSuccess, String registerId) {
                Log.i("faceImpl", "isRegisterSuccess==" + isRegisterSuccess);
                Log.i("faceImpl", "registerId==" + registerId);
            }

            @Override
            public void onFacePoint(float facePointX, float facePointY) {

            }

            @Override
            public void onFaceError() {
                Log.i("faceImpl", "onFaceError()");
            }
        });
    }

    private void addAlarm() {
        // 日期 + 时间 + 说的日期 + 说的时间 + 做什么事（中间以&连接）
        String data = "2016-11-01";
        String time = "16:06:19";
        String content = "开会";
        String tips = AlarmRemindManager.getRemindSpeakTips(AlarmRemindManager.getRemindInfo("", data, time, content));
        Log.i("alarm", "tips==" + tips);
    }

    private void playSystem() {
        local.play(MusicConfig.PLAY_MUSIC, "http://file.diyring.cc/UserRingWorksFile/0/50284096.mp3", new MusicCallBack() {
            @Override
            public void playStart() {
                Log.i("player", "playStart()");
            }

            @Override
            public void playComplected() {
                Log.i("player", "playComplected()");
            }

            @Override
            public void playFail() {
                Log.i("player", "playFail()");
            }
        });
    }

    private void playXima(int type, String content) {
        ximalaya.play(type, content, new MusicCallBack() {
            @Override
            public void playStart() {
                Log.i("player", "playStart()");
            }

            @Override
            public void playComplected() {
                Log.i("player", "playComplected()");
            }

            @Override
            public void playFail() {
                Log.i("player", "playFail()");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (intent != null) {
            stopService(intent);
        }
    }
}
