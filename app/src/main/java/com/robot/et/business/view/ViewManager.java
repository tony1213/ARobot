package com.robot.et.business.view;

import com.robot.et.business.view.callback.ViewCallBack;

/**
 * Created by houdeming on 2016/11/11.
 * view的接口回调设置
 */

public class ViewManager {

    private static ViewCallBack mCallBack;

    /**
     * 设置view的回调
     * @param callBack 回调接口对象
     */
    public static void setViewCallBack(ViewCallBack callBack) {
        mCallBack = callBack;
    }

    /**
     * 获取view的回调接口对象
     * @return
     */
    public static ViewCallBack getViewCallBack() {
        return mCallBack;
    }
}
