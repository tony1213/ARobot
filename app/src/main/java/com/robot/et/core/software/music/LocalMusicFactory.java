package com.robot.et.core.software.music;

import android.content.Context;

import com.robot.et.core.software.music.impl.local.LocalMusicPlayImpl;

/**
 * Created by houdeming on 2016/11/2.
 */
public class LocalMusicFactory implements MusicFactory {
    @Override
    public IMusic createMusic(Context context) {
        return new LocalMusicPlayImpl(context);
    }
}
