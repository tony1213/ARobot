package com.robot.et.business.control;

import android.text.TextUtils;
import android.util.Log;

import com.robot.et.business.control.orderenum.MoveEnum;

import java.util.Random;

/**
 * Created by houdeming on 2016/11/10.
 * 运动指令
 */

public class MoveOrder {

    private static final String TAG = "move";

    /**
     * 是否控制运动
     * @param result
     * @return
     */
    public static boolean isControlMove(String result) {
        if (!TextUtils.isEmpty(result)) {
            // 如果字数大于6个字数时，作为胡乱听得不处理
            if (result.length() > 6) {
                return false;
            }
            int moveKey = getMoveKey(result);
            Log.i(TAG, "moveKey===" + moveKey);
            if (moveKey != 0) {

                return true;
            }
        }
        return false;
    }

    /**
     * 获取控制运动的key
     * @param str
     * @return
     */
    public static int getMoveKey(String str) {
        int moveKey = 0;
        if (!TextUtils.isEmpty(str)) {
            for (MoveEnum moveEnum : MoveEnum.values()) {
                if (str.contains(moveEnum.getMoveName())) {
                    moveKey = moveEnum.getMoveKey();
                }
            }
        }
        return moveKey;
    }

    /**
     * 控制移动的时候，随机回答内容
     * @return
     */
    private static String getRandomAnswer() {
        String[] randomDatas = new String[]{"好的", "收到"};
        int randNum = new Random().nextInt(randomDatas.length);
        return randomDatas[randNum];
    }
}
