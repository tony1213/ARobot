package com.robot.et.core.software.music.impl.ximalaya;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.robot.et.core.software.music.IMusic;
import com.robot.et.core.software.music.callback.MusicCallBack;
import com.robot.et.core.software.music.config.MusicConfig;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioList;
import com.ximalaya.ting.android.opensdk.model.track.SearchTrackList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerConfig;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by houdeming on 2016/9/18.
 */
public class XiMaLaYaImpl implements IMusic {
    // 喜马拉雅AppSecret
    private static final String XIMALAYA_APPSECRET = "7fd5f74a6f3a86fe4a2266b653aa6522";
    private XmPlayerManager mPlayerManager;
    private CommonRequest mXimalaya;
    private MusicCallBack callBack;

    public XiMaLaYaImpl(Context context) {
        // 不希望SDK处理AudioFocus
        // 设置是否处理一般的audioFocus
        XmPlayerConfig.getInstance(context).setSDKHandleAudioFocus(false);
        // 设置是否处理电话拨出或进来时的AudioFocus
        XmPlayerConfig.getInstance(context).setSDKHandlePhoneComeAudioFocus(false);
        // 设置耳机线的插拔是的AudioFocus
        XmPlayerConfig.getInstance(context).setSDKHandleHeadsetPlugAudioFocus(false);
        // 初始化SDK
        mXimalaya = CommonRequest.getInstanse();
        mXimalaya.init(context, XIMALAYA_APPSECRET);
        // 初始化播放器
        mPlayerManager = XmPlayerManager.getInstance(context);
        // init()只需要调用一次，多次调用仅第一次有效
        mPlayerManager.init();
        // 监听播放状态
        mPlayerManager.addPlayerStatusListener(mPlayerStatusListener);
        mPlayerManager.setOnConnectedListerner(new XmPlayerManager.IConnectListener() {
            @Override
            public void onConnected() {
                Log.i(MusicConfig.MUSIC_TAG, "播放器初始化成功");
            }
        });
    }

    // 如果不接入播放器逻辑，则必须接入播放数据回传接口，否则喜马拉雅有权 直接封停接口
    private IXmPlayerStatusListener mPlayerStatusListener = new IXmPlayerStatusListener() {

        /** 切歌
         *
         * @param laModel 上一首model,可能为空 
         * @param curModel 下一首model 
         */
        @Override
        public void onSoundSwitch(PlayableModel laModel, PlayableModel curModel) {
            Log.i(MusicConfig.MUSIC_TAG, "onSoundSwitch");
        }

        @Override
        public void onSoundPrepared() {
            // 播放器准备完毕
            Log.i(MusicConfig.MUSIC_TAG, "onSoundPrepared");
        }

        @Override
        public void onSoundPlayComplete() {
            // 播放完成
            Log.i(MusicConfig.MUSIC_TAG, "onSoundPlayComplete");
            if (callBack != null) {
                callBack.playComplected();
            }
        }

        @Override
        public void onPlayStop() {
            // 停止播放
            Log.i(MusicConfig.MUSIC_TAG, "onPlayStop");
        }

        @Override
        public void onPlayStart() {
            // 开始播放
            Log.i(MusicConfig.MUSIC_TAG, "onPlayStart");
            if (callBack != null) {
                callBack.playStart();
            }
        }

        @Override
        public void onPlayProgress(int currPos, int duration) {
            // 播放进度回调
        }

        @Override
        public void onPlayPause() {
            // 暂停播放
            Log.i(MusicConfig.MUSIC_TAG, "onPlayPause");
        }

        @Override
        public boolean onError(XmPlayerException exception) {
            // 播放器错误
            Log.i(MusicConfig.MUSIC_TAG, "onError");
            if (callBack != null) {
                callBack.playFail();
            }
            return false;
        }

        @Override
        public void onBufferingStop() {
            // 结束缓冲
            Log.i(MusicConfig.MUSIC_TAG, "onBufferingStop");
        }

        @Override
        public void onBufferingStart() {
            // 开始缓冲
            Log.i(MusicConfig.MUSIC_TAG, "onBufferingStart");
        }

        @Override
        public void onBufferProgress(int percent) {
            // 缓冲进度回调
        }

    };

    @Override
    public void play(int playType, String content, MusicCallBack callBack) {
        this.callBack = callBack;
        if (!TextUtils.isEmpty(content)) {
            if (playType == MusicConfig.PLAY_MUSIC) {
                playMusic(content);
            } else {
                playRadio(content);
            }
        } else {
            if (callBack != null) {
                callBack.playFail();
            }
        }
    }

    @Override
    public void stopPlay() {
        if (mPlayerManager != null) {
            if (mPlayerManager.isPlaying()) {
                mPlayerManager.stop();
            }
        }
    }

    @Override
    public void destroyPlayer() {
        if (mPlayerManager != null) {
            mPlayerManager.removePlayerStatusListener(mPlayerStatusListener);
            mPlayerManager.release();
        }
    }

    // 播放音乐
    public void playMusic(String musicName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.SEARCH_KEY, musicName);
        CommonRequest.getSearchedTracks(map, new IDataCallBack<SearchTrackList>() {
            @Override
            public void onSuccess(SearchTrackList searchTrackList) {
                Log.i(MusicConfig.MUSIC_TAG, "playMusic onSuccess");
                List<Track> tracks = searchTrackList.getTracks();
                Log.i(MusicConfig.MUSIC_TAG, "tracks.size()===" + tracks.size());
                if (tracks != null && tracks.size() > 0) {
                    // 播放的音乐src
                    Log.i(MusicConfig.MUSIC_TAG, "track.getPlayUrl32()===" + tracks.get(0).getPlayUrl32());
                    // 播放的名字
                    Log.i(MusicConfig.MUSIC_TAG, "track.getTrackTitle()===" + tracks.get(0).getTrackTitle());
                    // 默认播放第一个
                    mPlayerManager.playList(tracks, 0);
                } else {
                    if (callBack != null) {
                        callBack.playFail();
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.i(MusicConfig.MUSIC_TAG, "playMusic onError");
                if (callBack != null) {
                    callBack.playFail();
                }
            }
        });
    }

    // 播放电台
    public void playRadio(String radioName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.SEARCH_KEY, radioName);
        CommonRequest.getSearchedRadios(map, new IDataCallBack<RadioList>() {
            @Override
            public void onSuccess(RadioList radioList) {
                Log.i(MusicConfig.MUSIC_TAG, "playRadio onSuccess");
                List<Radio> radios = radioList.getRadios();
                Log.i(MusicConfig.MUSIC_TAG, "radios.size()===" + radios.size());
                if (radios != null && radios.size() > 0) {
                    // 默认播放第一个
                    Radio radio = radios.get(0);
                    // 电台的名字
                    Log.i(MusicConfig.MUSIC_TAG, "radio.getRadioName()==" + radio.getRadioName());
                    mPlayerManager.playRadio(radio);
                } else {
                    if (callBack != null) {
                        callBack.playFail();
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.i(MusicConfig.MUSIC_TAG, "playRadio onError");
                if (callBack != null) {
                    callBack.playFail();
                }
            }
        });
    }
}
