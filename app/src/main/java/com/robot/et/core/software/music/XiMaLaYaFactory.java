package com.robot.et.core.software.music;

import android.content.Context;

import com.robot.et.core.software.music.impl.ximalaya.XiMaLaYaImpl;

/**
 * Created by houdeming on 2016/11/2.
 */
public class XiMaLaYaFactory implements MusicFactory {
    @Override
    public IMusic createMusic(Context context) {
        return new XiMaLaYaImpl(context);
    }
}
