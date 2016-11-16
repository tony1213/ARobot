package com.robot.et.test.vision;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.robot.et.R;
import com.robot.et.VisionManager;
import com.robot.et.callback.VisionCallBack;
import org.openni.DeviceInfo;

import org.openni.NativeMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by houdeming on 2016/11/12.
 */

public class VisionActivity extends Activity implements View.OnClickListener,VisionCallBack {

    private static final String TAG = "visionTest";
    private VisionManager visionManager;
    private String mActionUsbPermission = "act.usb.action";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision_test);
        // 保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Button init = (Button) findViewById(R.id.btn_init);
        Button uInit = (Button) findViewById(R.id.btn_uinit);
        Button learn = (Button) findViewById(R.id.btn_learn);
        Button call = (Button) findViewById(R.id.btn_callback);
        Button pos = (Button) findViewById(R.id.btn_bodyPos);
        Button recog = (Button) findViewById(R.id.btn_recog);
        Button learnOpen = (Button) findViewById(R.id.btn_learn_open);
        Button learnClose = (Button) findViewById(R.id.btn_learn_close);
        Button posOpen = (Button) findViewById(R.id.btn_bodyPos_open);
        Button posClose = (Button) findViewById(R.id.btn_bodyPos_close);
        init.setOnClickListener(this);
        uInit.setOnClickListener(this);
        learn.setOnClickListener(this);
        call.setOnClickListener(this);
        pos.setOnClickListener(this);
        recog.setOnClickListener(this);
        learnOpen.setOnClickListener(this);
        learnClose.setOnClickListener(this);
        posOpen.setOnClickListener(this);
        posClose.setOnClickListener(this);

        visionManager = new VisionManager(this);

        //注册USB设备权限管理广播
        IntentFilter filter = new IntentFilter(mActionUsbPermission);
        registerReceiver(usbReceiver, filter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
                this.mActionUsbPermission), 0);

//        List<DeviceInfo> devices = enumerateDevices();
//        if (devices.isEmpty()) {
//            Log.i(TAG, "devices.isEmpty()");
//            return;
//        }
//
//        String uri = devices.get(0).getUri();
//        Log.i(TAG, "uri==" + uri);
//
//        UsbDevice usbDevice = getUsbDevice(uri);

        DeviceInfo info = new DeviceInfo("04b4/1003@3/19", "", "/dev/bus/usb/003/004", 7463, 1537);

        UsbDevice usbDevice = getUsbDevice(info);

        if (usbDevice != null) {
            Log.i(TAG, "usbDevice != null");
//            UsbManager manager = (UsbManager)this.getSystemService("usb");
//            manager.requestPermission(usbDevice, permissionIntent);
        } else {
            Log.i(TAG, "usbDevice == null");
        }
    }

    public UsbDevice getUsbDevice(String uri)
    {
        List devices = enumerateDevices();
        Iterator iterator = devices.iterator();
        while (iterator.hasNext()) {
            DeviceInfo deviceInfo = (DeviceInfo)iterator.next();
            if (deviceInfo.getUri().compareTo(uri) == 0) {
                return getUsbDevice(deviceInfo);
            }
        }

        return null;
    }

    public static List<DeviceInfo> enumerateDevices() {
        List devices = new ArrayList();
        NativeMethods.checkReturnStatus(NativeMethods.oniGetDeviceList(devices));
        return devices;
    }

    public UsbDevice getUsbDevice(DeviceInfo deviceInfo) {
        UsbManager manager = (UsbManager)this.getSystemService("usb");
        HashMap deviceList = manager.getDeviceList();
        Iterator iterator = deviceList.values().iterator();

        while (iterator.hasNext()) {
            UsbDevice usbDevice = (UsbDevice)iterator.next();

            if ((usbDevice.getVendorId() == deviceInfo.getUsbVendorId()) && (usbDevice.getProductId() == deviceInfo.getUsbProductId())) {
                return usbDevice;
            }
        }

        return null;
    }

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "action==" + action);
            if (mActionUsbPermission.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device == null) {
                        Log.i(TAG, "111device == null");
                        return;
                    }

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                        }
                        try {

                        } catch (Exception e) {
                            Log.i(TAG, "Exception");
                        }

                    } else {
                        Log.i(TAG, "用户不允许USB访问设备，程序退出");
                    }
                }
            }
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(usbReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_init:
                try {
                    int visionId = visionManager.visionInit();
                    Log.i(TAG,"visionId==" + visionId);
                } catch (RemoteException e) {
                    Log.i(TAG,"RemoteException");
                }
                break;
            case R.id.btn_uinit:
                try {
                    Log.i(TAG,"visionUninit");
                    visionManager.visionUninit();
                } catch (RemoteException e) {
                    Log.i(TAG,"visionUninit RemoteException");
                }

                break;
            case R.id.btn_learn_open:
                try {
                    Log.i(TAG,"visionLearnOpen");
                    visionManager.visionLearnOpen();
                } catch (RemoteException e) {
                    Log.i(TAG,"Learn RemoteException");
                }
                break;
            case R.id.btn_learn_close:
                try {
                    Log.i(TAG,"visionLearnClose");
                    visionManager.visionLearnClose();
                } catch (RemoteException e) {
                    Log.i(TAG,"Learn RemoteException");
                }
                break;
            case R.id.btn_learn:
                try {
                    Log.i(TAG,"Learn");
                    String str = "android";
                    visionManager.objLearnStartLearn(str);
                } catch (RemoteException e) {
                    Log.i(TAG,"Learn RemoteException");
                }
                break;
            case R.id.btn_callback:
                try {
                    Log.i(TAG,"Callback");
                    visionManager.testCallback();
                } catch (RemoteException e) {
                    Log.i(TAG,"Callback RemoteException");
                }
                break;
            case R.id.btn_bodyPos_open:
                try {
                    Log.i(TAG,"bodyDetectOpen");
                    visionManager.bodyDetectOpen();
                } catch (RemoteException e) {
                    Log.i(TAG,"bodyDetectGetPos RemoteException");
                }
                break;
            case R.id.btn_bodyPos_close:
                try {
                    Log.i(TAG,"bodyDetectClose");
                    visionManager.bodyDetectClose();
                } catch (RemoteException e) {
                    Log.i(TAG,"bodyDetectGetPos RemoteException");
                }
                break;
            case R.id.btn_bodyPos:
                try {
                    Log.i(TAG,"bodyDetectGetPos");
                    visionManager.bodyDetectGetPos();
                } catch (RemoteException e) {
                    Log.i(TAG,"bodyDetectGetPos RemoteException");
                }
                break;
            case R.id.btn_recog:
                try {
                    Log.i(TAG,"Recog");
                    visionManager.objLearnStartRecog();
                } catch (RemoteException e) {
                    Log.i(TAG,"Recog RemoteException");
                }
                break;
        }
    }

    @Override
    public void learnOpenEnd() {
        Log.i(TAG,"learnOpenEnd");
    }

    @Override
    public void learnWaring(int id) {
        Log.i(TAG,"learnWaring id==" + id);
    }

    @Override
    public void learnEnd() {
        Log.i(TAG,"learnEnd");
    }

    @Override
    public void learnRecogniseEnd(String name, int conf) {
        Log.i(TAG,"learnRecogniseEnd name==" + name + "---conf===" + conf);
    }

    @Override
    public void bodyPosition(float centerX, float centerY, float centerZ) {
        Log.i(TAG,"X==" + centerX + "--Y==" + centerY + "--Z==" + centerZ);
    }
}
