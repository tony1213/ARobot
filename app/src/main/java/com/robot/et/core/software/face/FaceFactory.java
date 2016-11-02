package com.robot.et.core.software.face;

import android.content.Context;

/**
 * Created by houdeming on 2016/11/2.
 * 创建IFace的工厂接口
 */
public interface FaceFactory {
    /**
     * 创建IFace对象
     *
     * @param context 上下文
     * @return
     */
    IFace createFace(Context context);
}
