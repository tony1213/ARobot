package com.robot.et.core.hardware.light;

/**
 * 照明灯
 */
public class FloodLight {

	static {
		System.loadLibrary("FloodLight");
	}

	/**
	 * 初始化照明灯
	 * @return
     */
	public static native int initFloodLight();

	/**
	 * 设置照明灯的状态
	 * @param lightStatus 照明灯的状态值
	 * @return
     */
	public static native int setLightStatus(int lightStatus);
}
