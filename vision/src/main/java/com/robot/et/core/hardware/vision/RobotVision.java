package com.robot.et.core.hardware.vision;

import android.util.Log;

import com.robot.et.callback.VisionCallBack;

public class RobotVision {

	private static final String TAG = "visionLib";
	private static VisionCallBack mCallBack;

	static {
		System.loadLibrary("roVisionCore");
	}

	public static void setVisionCallBack(VisionCallBack callBack) {
		mCallBack = callBack;
	}

	public static void callback_learnOpenEnd()
	{
		Log.i(TAG,"learnOpenEnd");
		if (mCallBack != null) {
			mCallBack.learnOpenEnd();
		}
	}

	public static void callback_learnWarning(int id)
	{
		Log.i(TAG,"learnWarning id:"+id);
		if (mCallBack != null) {
			mCallBack.learnWaring(id);
		}
	}

	public static void callback_learnLearnEnd()
	{
		Log.i(TAG,"learnLearnEnd");
		if (mCallBack != null) {
			mCallBack.learnEnd();
		}
	}

	public static void callback_learnRecogEnd(String name, int conf)
	{
		Log.i(TAG,"learnRecogEnd name:"+name + " conf:"+conf);
		if (mCallBack != null) {
			mCallBack.learnRecogniseEnd(name, conf);
		}
	}

	public static class Postion3Df {
		public float centerX;
		public float centerY;
		public float centerZ;
	}

	/**
	 * 视觉初始化
	 */
	public static native int visionInit();
	
	/**
	 * 视觉反初始化
	 */
	public static native void visionUninit();

	public static native void objLearnStartLearn(String str);

	public static native void objLearnStartRecog();

	public static native void testCallback();

	public static native void bodyDetectGetPos(Postion3Df pos);
}
