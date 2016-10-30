package com.robot.et.core.software.video.impl.agora;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robot.et.R;
import com.robot.et.app.CustomApplication;
import com.robot.et.core.software.video.common.VideoConfig;

import java.util.Random;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

public class AgoraActivity extends BaseEngineEventHandlerActivity {
    private final String TAG = "video";
    public final static String CALL_TYPE = "call_type";
    public final static String CALL_CHANNEL_ID = "call_channel_id";
    public final static String CALL_IS_VOICE = "call_is_voice";
    private int mCallingType;
    private String channelId;
    private boolean isVoiceCall;
    private SurfaceView mLocalView;
    private LinearLayout mRemoteUserContainer;
    private RtcEngine rtcEngine;
    //判断用户是否接通 默认不接通
    private static boolean isUserJoined;
    //查看
    private boolean isLook;
    private int mLastRxBytes = 0;
    private int mLastTxBytes = 0;
    private int mLastDuration = 0;
    private LinearLayout localViewContainer;
    public static AgoraActivity instance;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_agora_room);
        // 保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        instance = this;
        Intent mIntent = getIntent();
        mCallingType = mIntent.getIntExtra(CALL_TYPE, 0);
        channelId = mIntent.getStringExtra(CALL_CHANNEL_ID);
        isVoiceCall = mIntent.getBooleanExtra(CALL_IS_VOICE, false);
        Log.i(TAG, " mCallingType===" + mCallingType);
        Log.i(TAG, " channelId===" + channelId);
        Log.i(TAG, " isVoiceCall===" + isVoiceCall);
        // 初始化声网的RtcEngine对象
        setupRtcEngine();

        rtcEngine.setEnableSpeakerphone(true);
        mRemoteUserContainer = (LinearLayout) findViewById(R.id.user_remote_views);
        setRemoteUserViewVisibility(false);
        isLook = false;
        switch (mCallingType) {
            case VideoConfig.CALL_TYPE_VIDEO:// 视频
                video();
                break;
            case VideoConfig.CALL_TYPE_VOICE:// 语音
                voice();
                break;
            case VideoConfig.CALL_TYPE_LOOK:// 查看
                isLook = true;
                video();
                break;
        }

        isUserJoined = false;
        // 主动呼叫时
        if (isVoiceCall) {
            //播放打电话提示音（只在主动呼叫时播放）
            //15秒如果用户没有接通的话，自动挂断电话
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (!isUserJoined) {
                        closeChannel();
                        // 通知APP关掉通话
                        finish();
                    }
                }
            }, 15 * 1000);
        }

    }

    //退出当前通话
    public void closeChannel() {
        setRemoteUserViewVisibility(false);
        leaveChannel();
        isUserJoined = true;
        finish();
    }

    //加入通话频道
    private void setupChannel() {
        rtcEngine.joinChannel(VideoConfig.AGORA_KEY, channelId, "", new Random().nextInt(Math.abs((int) System.currentTimeMillis())));
    }

    // 初始化声网的RtcEngine对象
    private void setupRtcEngine() {
        rtcEngine = CustomApplication.getInstance().getRtcEngine();
        CustomApplication.getInstance().setEngineEventHandlerActivity(this);
        rtcEngine.enableVideo();
    }

    //语音视频通话的view
    @SuppressWarnings("static-access")
    private void ensureLocalViewIsCreated() {
        if (this.mLocalView == null) {
            localViewContainer = (LinearLayout) findViewById(R.id.user_local_view);
            SurfaceView localView = rtcEngine.CreateRendererView(getApplicationContext());
            this.mLocalView = localView;
            localViewContainer.addView(localView, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

            rtcEngine.enableVideo();
            rtcEngine.setupLocalVideo(new VideoCanvas(this.mLocalView));
        }
    }

	/*
     * rtcEngine.muteLocalAudioStream(false);//静音
	 * rtcEngine.setEnableSpeakerphone(false);// 扬声器
	 * rtcEngine.muteLocalVideoStream(false);// 关闭摄像头
	 * rtcEngine.switchCamera();// 切换摄像头
	 */

    //语音通话
    private void voice() {
        mCallingType = VideoConfig.CALL_TYPE_VOICE;

        ensureLocalViewIsCreated();

        rtcEngine.setEnableSpeakerphone(true);// 扬声器开
        rtcEngine.muteAllRemoteAudioStreams(false);
        rtcEngine.muteLocalAudioStream(false);
        rtcEngine.disableVideo();
        rtcEngine.muteLocalVideoStream(true);
        rtcEngine.muteAllRemoteVideoStreams(true);

        if (mRemoteUserContainer.getChildCount() == 0) {
            setupChannel();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateRemoteUserViews(VideoConfig.CALL_TYPE_VOICE);
            }
        }, 500);

    }

    //视频通话
    private void video() {
        ensureLocalViewIsCreated();

        if (mCallingType == VideoConfig.CALL_TYPE_VIDEO) { // 视频
            rtcEngine.setEnableSpeakerphone(true);// 扬声器开
            rtcEngine.muteAllRemoteAudioStreams(false);
        } else if (mCallingType == VideoConfig.CALL_TYPE_LOOK) { // 查看
            rtcEngine.setEnableSpeakerphone(false);// 扬声器关
            rtcEngine.muteAllRemoteAudioStreams(true);//静音所有远端音频
        }

        mCallingType = VideoConfig.CALL_TYPE_VIDEO;

        rtcEngine.enableVideo();
        rtcEngine.muteLocalVideoStream(false);
        rtcEngine.muteLocalAudioStream(false);
        rtcEngine.muteAllRemoteVideoStreams(false);
        //设置本地视频属性
        rtcEngine.setVideoProfile(41);

        if (mRemoteUserContainer.getChildCount() == 0) {
            setupChannel();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateRemoteUserViews(VideoConfig.CALL_TYPE_VIDEO);
            }
        }, 500);

    }

    private void setRemoteUserViewVisibility(boolean isVisible) {
        findViewById(R.id.user_remote_views).getLayoutParams().height = isVisible ? (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, LinearLayout.LayoutParams.MATCH_PARENT, getResources().getDisplayMetrics()) : 0;
    }

    //切换视频音频通话时，更新 view 的显示。只是更新重用的 view，并不新添加
    private void updateRemoteUserViews(int callingType) {
        int visibility = View.GONE;
        if (callingType == VideoConfig.CALL_TYPE_VIDEO) {
            visibility = View.GONE;
        } else if (callingType == VideoConfig.CALL_TYPE_VOICE) {
            visibility = View.VISIBLE;
        }

        for (int i = 0, size = mRemoteUserContainer.getChildCount(); i < size; i++) {

            View singleRemoteView = mRemoteUserContainer.getChildAt(i);
            singleRemoteView.findViewById(R.id.remote_user_voice_container).setVisibility(visibility);

            if (callingType == VideoConfig.CALL_TYPE_VIDEO) {
                FrameLayout remoteVideoUser = (FrameLayout) singleRemoteView.findViewById(R.id.viewlet_remote_video_user);
                if (remoteVideoUser.getChildCount() > 0) {
                    final SurfaceView remoteView = (SurfaceView) remoteVideoUser.getChildAt(0);
                    if (remoteView != null) {
                        remoteView.setZOrderOnTop(true);
                        remoteView.setZOrderMediaOverlay(true);
                        int savedUid = (Integer) remoteVideoUser.getTag();
                        rtcEngine.setupRemoteVideo(new VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_HIDDEN, savedUid));
                    }
                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
        instance = null;
        isUserJoined = true;
    }

    //自己离开通话频道
    private void leaveChannel() {
        rtcEngine.leaveChannel();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    //更新码率
    public void onUpdateSessionStats(final IRtcEngineEventHandler.RtcStats stats) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 码率
                int kbs = ((stats.txBytes + stats.rxBytes - mLastTxBytes - mLastRxBytes) / 1024 / (stats.totalDuration - mLastDuration + 1));
                mLastRxBytes = stats.rxBytes;
                mLastTxBytes = stats.txBytes;
                mLastDuration = stats.totalDuration;
//                Log.i(TAG, "kbs===" + kbs);
            }
        });
    }

    //远端视频接收解码回调
    public synchronized void onFirstRemoteVideoDecoded(final int uid, int width, int height, final int elapsed) {
        Log.i(TAG, "onFirstRemoteVideoDecoded");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View remoteUserView = mRemoteUserContainer.findViewById(Math.abs(uid));
                if (remoteUserView == null) {
                    LayoutInflater layoutInflater = getLayoutInflater();
                    View singleRemoteUser = layoutInflater.inflate(R.layout.viewlet_remote_user, null);
                    singleRemoteUser.setId(Math.abs(uid));

                    TextView username = (TextView) singleRemoteUser.findViewById(R.id.remote_user_name);
                    username.setText(String.valueOf(uid));

                    mRemoteUserContainer.addView(singleRemoteUser, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

                    remoteUserView = singleRemoteUser;
                }

                FrameLayout remoteVideoUser = (FrameLayout) remoteUserView.findViewById(R.id.viewlet_remote_video_user);
                remoteVideoUser.removeAllViews();
                remoteVideoUser.setTag(uid);

                // ensure remote video view setup
                final SurfaceView remoteView = RtcEngine.CreateRendererView(getApplicationContext());
                remoteVideoUser.addView(remoteView,new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                remoteView.setZOrderOnTop(true);
                remoteView.setZOrderMediaOverlay(true);

                rtcEngine.enableVideo();
                int successCode = rtcEngine.setupRemoteVideo(new VideoCanvas(
                        remoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));

                if (successCode < 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rtcEngine.setupRemoteVideo(new VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
                            remoteView.invalidate();
                        }
                    }, 500);
                }

                if (remoteUserView != null && mCallingType == VideoConfig.CALL_TYPE_VIDEO) {
                    remoteUserView.findViewById(R.id.remote_user_voice_container).setVisibility(View.GONE);
                } else {
                    remoteUserView.findViewById(R.id.remote_user_voice_container).setVisibility(View.VISIBLE);
                }

                setRemoteUserViewVisibility(true);
            }
        });

    }

    // 其他用户加入当前频道回调
    public synchronized void onUserJoined(final int uid, int elapsed) {
        Log.i(TAG, "onUserJoined");
        View existedUser = mRemoteUserContainer.findViewById(Math.abs(uid));
        if (existedUser != null) {
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 防止声音提示还有，停止掉

                View singleRemoteUser = mRemoteUserContainer.findViewById(Math.abs(uid));
                if (singleRemoteUser != null) {
                    return;
                }

                isUserJoined = true;

                LayoutInflater layoutInflater = getLayoutInflater();
                singleRemoteUser = layoutInflater.inflate(R.layout.viewlet_remote_user, null);
                singleRemoteUser.setId(Math.abs(uid));

                TextView username = (TextView) singleRemoteUser.findViewById(R.id.remote_user_name);
                username.setText(String.valueOf(uid));

                mRemoteUserContainer.addView(singleRemoteUser, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));

                setRemoteUserViewVisibility(true);
                rtcEngine.setRemoteRenderMode(uid, 1);
            }
        });

    }

    // 其他用户离开当前频道回调
    public void onUserOffline(final int uid) {
        Log.i(TAG, "onUserOffline");

        if (mRemoteUserContainer == null) {
            Log.i(TAG, "mRemoteUserContainer == null");
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View userViewToRemove = mRemoteUserContainer.findViewById(Math.abs(uid));
                mRemoteUserContainer.removeView(userViewToRemove);

                Log.i(TAG, "当前房间还剩余人数======" + mRemoteUserContainer.getChildCount());
                //当前没人视频或者通话
                if (mRemoteUserContainer.getChildCount() == 0) {
                    closeChannel();
                }
            }
        });

    }

    // 离开频道回调
    @Override
    public void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats) {
        Log.i(TAG, "onLeaveChannel");
        try {
            finish();
        } catch (Exception e) {
            Log.i(TAG, "onLeaveChannel Exception==" + e.getMessage());
        }
    }

    // 用户停止/重启视频回调
    public void onUserMuteVideo(final int uid, final boolean muted) {
        Log.i(TAG, "onUserMuteVideo");
        if (isFinishing()) {
            return;
        }

        if (mRemoteUserContainer == null) {
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View remoteView = mRemoteUserContainer.findViewById(Math.abs(uid));
                remoteView.findViewById(R.id.remote_user_voice_container)
                        .setVisibility((VideoConfig.CALL_TYPE_VOICE == mCallingType || (VideoConfig.CALL_TYPE_VIDEO == mCallingType && muted)) ? View.VISIBLE : View.GONE);
                remoteView.invalidate();
            }
        });

    }

    // 发生错误回调
    @Override
    public synchronized void onError(int err) {
        Log.i(TAG, "onError   err====" + err);
        if (isFinishing()) {//界面是否finish
            return;
        }
        //不是查看的时候报异常问题
        if (!isLook) {
            if (err == 101) {
                // 抱歉，声网key异常
                finish();
            } else if (err == 104) {
                // 抱歉，网络异常
                finish();
            }
        }
    }
}