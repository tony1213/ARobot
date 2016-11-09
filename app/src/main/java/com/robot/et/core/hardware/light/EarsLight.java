package com.robot.et.core.hardware.light;

/**
 * 耳朵灯
 */
public class EarsLight {

	static {
		System.loadLibrary("EarsLight");
	}

	/**
	 * 初始化耳朵灯
	 * @return
     */
	public static native int initEarsLight();

	/**
	 * 设置耳朵等的状态
	 * @param lightStatus 耳朵灯状态值
	 * @return
     */
	public static native int setLightStatus(int lightStatus);
}
