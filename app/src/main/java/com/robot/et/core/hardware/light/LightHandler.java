package com.robot.et.core.hardware.light;

import android.util.Log;

/**
 * 对照明灯与耳朵灯的设置
 */
public class LightHandler {
    private static final String TAG = "light";
    private static boolean isStart = false;

    static {
        // 初始化耳朵灯
        int earsLightFd = EarsLight.initEarsLight();
        Log.i(TAG, "earsLightFd==" + earsLightFd);
        // 初始化照明灯
        int floodLightFd = FloodLight.initFloodLight();
        Log.i(TAG, "floodLightFd==" + floodLightFd);
    }

    /**
     * 设置耳朵灯的状态
     * @param lightState 耳朵灯状态值
     */
    public static void setEarsLight(int lightState) {
        Log.i(TAG, "setEarsLight lightState==" + lightState);
        isStart = false;
        switch (lightState) {
            case EarsLightConfig.EARS_CLOSE:
                EarsLight.setLightStatus(lightState);

                break;
            case EarsLightConfig.EARS_BRIGHT:
                EarsLight.setLightStatus(lightState);

                break;
            case EarsLightConfig.EARS_BLINK:
                isStart = true;
                controlEarsLight(lightState);

                break;
            case EarsLightConfig.EARS_CLOCKWISE_TURN:
                isStart = true;
                controlEarsLight(lightState);

                break;
            case EarsLightConfig.EARS_ANTI_CLOCKWISE_TURN:
                isStart = true;
                controlEarsLight(lightState);

                break;
            case EarsLightConfig.EARS_HORSE_RACE_LAMP:
                isStart = true;
                controlEarsLight(lightState);

                break;
            default:
                break;
        }
    }

    /**
     * 设置照明灯的状态
     * @param lightState 照明灯的状态值
     */
    public static void setFloodLight(int lightState) {
        Log.i(TAG, "setFloodLight lightState==" + lightState);
        FloodLight.setLightStatus(lightState);
    }

    /**
     * 控制耳朵灯
     * @param lightState
     */
    private static void controlEarsLight(final int lightState) {
        // 不要用timer计时器来控制，如果时间较长的话，会对其他线程造成影响
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isStart) {
                    Log.i(TAG, "controlEarsLight lightState==" + lightState);
                    EarsLight.setLightStatus(lightState);
                    try {
                        Thread.sleep(5000);// 5s执行一次（时间太短的话可能会造成影响）
                    } catch (InterruptedException e) {
                        Log.i(TAG, "controlEarsLight InterruptedException==" + e.getMessage());
                    }
                }
            }
        }).start();
    }
}
