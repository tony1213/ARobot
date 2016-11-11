package com.robot.et.business.view.callback;

import android.graphics.Bitmap;

/**
 * Created by houdeming on 2016/11/11.
 * view的回调
 */

public interface ViewCallBack {
    /**
     * 显示文字
     * @param content 文字内容
     */
    void onShowText(String content);

    /**
     * 显示表情
     * @param isEmotionAnim 是否是表情动画
     * @param resId 表情资源ID
     */
    void onShowEmotion(boolean isEmotionAnim, int resId);

    /**
     * 显示图片
     * @param isNeedAnim 是否需要动画
     * @param bitmap 图片的bitmap
     */
    void onShowImg(boolean isNeedAnim, Bitmap bitmap);
}
