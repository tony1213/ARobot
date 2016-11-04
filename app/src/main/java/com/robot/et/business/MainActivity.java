package com.robot.et.business;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.robot.et.R;
import com.robot.et.base.BaseActivity;
import com.robot.et.business.voice.VoiceService;
import com.robot.et.core.software.camera.ICamera;
import com.robot.et.core.software.camera.LocalCameraFactory;
import com.robot.et.core.software.camera.callback.CameraCallBack;
import com.robot.et.core.software.face.IFace;
import com.robot.et.core.software.face.IflyFaceFactory;
import com.robot.et.core.software.face.callback.FaceCallBack;
import com.robot.et.core.software.music.IMusic;
import com.robot.et.core.software.music.LocalMusicFactory;
import com.robot.et.core.software.music.XiMaLaYaFactory;
import com.robot.et.core.software.music.callback.MusicCallBack;
import com.robot.et.core.software.music.config.MusicConfig;
import com.robot.et.core.software.videocall.AgoraFactory;
import com.robot.et.core.software.videocall.IVideoCall;
import com.robot.et.core.software.videocall.config.VideoCallConfig;
import com.robot.et.core.software.videoplay.IVideoPlay;
import com.robot.et.core.software.videoplay.LocalVideoPlayFactory;
import com.robot.et.core.software.videoplay.callback.VideoPlayCallBack;
import com.robot.et.core.software.system.alarm.AlarmRemindManager;

import java.io.File;

public class MainActivity extends BaseActivity implements View.OnClickListener {

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
        setContentView(R.layout.activity_main_open);
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);
        Button button6 = (Button) findViewById(R.id.button6);
        Button button7 = (Button) findViewById(R.id.button7);
        Button button8 = (Button) findViewById(R.id.button8);
        Button button9 = (Button) findViewById(R.id.button9);
        Button button10 = (Button) findViewById(R.id.button10);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button10.setOnClickListener(this);

        face = new IflyFaceFactory().createFace(this);
        systemCamera = new LocalCameraFactory().createCamera(this);
        local = new LocalMusicFactory().createMusic(this);
        ximalaya = new XiMaLaYaFactory().createMusic(this);
        videoCall = new AgoraFactory().createVideoCall(this);
        videoPlay = new LocalVideoPlayFactory().createVideoPlay(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                playSystem();
                break;
            case R.id.button2:
                playXima(MusicConfig.PLAY_MUSIC, "青花瓷");
                break;
            case R.id.button3:
                playXima(MusicConfig.PLAY_RADIO, "江西音乐广播");
                break;
            case R.id.button4:
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
            case R.id.button5:
                local.stopPlay();
                ximalaya.stopPlay();
                videoPlay.stopPlay();
                systemCamera.closeCamera();
                break;
            case R.id.button6:
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
            case R.id.button7:
                addAlarm();
                break;
            case R.id.button8:
                face();
                break;
            case R.id.button9:
                videoCall.callPhone(VideoCallConfig.CALL_TYPE_VIDEO, "123", true);
                break;
            case R.id.button10:
                intent = new Intent(this, VoiceService.class);
                startService(intent);
                break;
        }
    }

    private void face() {
        face.openFaceDistinguish(true, null, new FaceCallBack() {

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
