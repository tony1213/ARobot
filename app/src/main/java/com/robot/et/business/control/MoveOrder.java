package com.robot.et.business.control;

import android.text.TextUtils;
import android.util.Log;

import com.robot.et.business.control.orderenum.MoveEnum;
import com.robot.et.config.GlobalConfig;
import com.robot.et.core.software.slam.SlamtecLoader;
import com.robot.et.util.Utilities;

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
            int moveKey = getMoveKey(result);
            Log.i(TAG, "moveKey===" + moveKey);
            if (moveKey != 0) {
                String content = getRandomAnswer();
                int digit = Utilities.chineseNum2Int(result);
                if (moveKey == MoveEnum.LEFT.getMoveKey() || moveKey == MoveEnum.RIGHT.getMoveKey()) {
                    // 左转右转
                    if (digit == 0) {
                        digit = 90;// 默认90度
                    }
                    if (GlobalConfig.isConnectSlam) {
                        SlamtecLoader.getInstance().execBasicRotate(digit,moveKey,0);
                    }
                } else if (moveKey == MoveEnum.TURN_AFTER.getMoveKey()) {// 向后转
                    digit = 180;
                    if (GlobalConfig.isConnectSlam) {
                        SlamtecLoader.getInstance().execBasicRotate(digit,3,0);
                    }
                } else {// 前进
//                    if (digit == 0) {
//                        digit = 1 * 1000;// 默认1米
////                        SlamtecLoader.getInstance().execBasicMove(1);
////                            float currentRobotX = SlamtecLoader.getInstance().getCurrentRobotPose().getX();
////                            float currentRobotY = SlamtecLoader.getInstance().getCurrentRobotPose().getY();
////                            SlamtecLoader.getInstance().execSetGoal(currentRobotX+digit,currentRobotY);
//                        SlamtecLoader.getInstance().execSetGoal(1,0);
//                    } else {
//                        digit *= 1000;// 单位是mm
//                    }
                    if (GlobalConfig.isConnectSlam) {
                        SlamtecLoader.getInstance().execBasicMove(MoveEnum.STOP.getMoveKey());
                    }
                }
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
