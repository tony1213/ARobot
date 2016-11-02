package com.robot.et.core.software.videoplay;

import android.content.Context;

import com.robot.et.core.software.videoplay.impl.local.LocalVideoPlayImpl;

/**
 * Created by houdeming on 2016/11/2.
 */
public class LocalVideoPlayFactory implements VideoPlayFactory {
    @Override
    public IVideoPlay createVideoPlay(Context context) {
        return new LocalVideoPlayImpl(context);
    }
}