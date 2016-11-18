package com.robot.et.business.media;

import android.content.Context;
import android.util.Log;

import com.robot.et.config.GlobalConfig;
import com.robot.et.core.software.music.IMusic;
import com.robot.et.core.software.music.MusicFactory;
import com.robot.et.core.software.music.callback.MusicCallBack;

/**
 * Created by houdeming on 2016/11/16.
 * 音乐
 */

public class Music {

    private static final String TAG = "musicPlay";
    private static IMusic localPlay;
    private static IMusic xiMaLaYa;

    public static void playMusic(Context context, int playType, String content) {
        GlobalConfig.isPlayMusic = true;
        playByLocal(context, playType, content);
    }

    private static void playByLocal(Context context, int playType, String content) {
        if (localPlay == null) {
            localPlay = MusicFactory.produceLocalPlay(context);
        }
        localPlay.play(playType, content, musicCallBack);
    }

    private static void playByXiMaLaYa(Context context, int playType, String content) {
        if (xiMaLaYa == null) {
            xiMaLaYa = MusicFactory.produceXiMaLaYaPlay(context);
        }
        xiMaLaYa.play(playType, content, musicCallBack);
    }

    public static void stopMusic() {
        if (localPlay != null) {
            localPlay.stopPlay();
        }
        if (xiMaLaYa != null) {
            xiMaLaYa.stopPlay();
        }
    }

    private static MusicCallBack musicCallBack = new MusicCallBack() {
        @Override
        public void playStart() {
            Log.i(TAG, "playStart()");
        }

        @Override
        public void playComplected() {
            Log.i(TAG, "playComplected()");
            GlobalConfig.isPlayMusic = false;
        }

        @Override
        public void playFail() {
            Log.i(TAG, "playFail()");
            GlobalConfig.isPlayMusic = false;
        }
    };
}
