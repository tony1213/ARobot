package com.robot.et.core.hardware.vision;

import android.graphics.Bitmap;
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

	public static class ImageInfo {
		public int width;
		public int height;
		public int dataType;
	}

	public static native int visionInit();
	
	public static native void visionUninit();

	public static native void objLearnOpen();

	public static native void objLearnClose();

	public static native void objLearnStartLearn(String str);

	public static native void objLearnStartRecog();

	public static native void testCallback();

	public static native void bodyDetectOpen();

	public static native void bodyDetectClose();

	public static native void bodyDetectGetPos(Postion3Df pos);

	public static native int  visionImageGetInfo(ImageInfo info);

	public static native int  visionImageGetData(Bitmap bmp);
}
