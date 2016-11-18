package com.robot.et.business.vision.callback;

import android.graphics.Bitmap;

/**
 * Created by houdeming on 2016/11/18.
 */

public interface VisionBitmapCallBack {
    /**
     * 获取视觉图像
     * @param bitmap
     */
    void onVisionBitmap(Bitmap bitmap);
}
