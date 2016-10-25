package com.robot.et.core.hardware.serialport;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.robot.et.core.hardware.serialport.SerialPortUtil.OnDataReceiveListener;

public class SerialPortHandler implements OnDataReceiveListener {

    private static final String TAG = "SerialPort";

    private static SerialPortUtil instance;
    private Context context;

    //缓存数据
    private static StringBuffer buffer = new StringBuffer(1024);

    public SerialPortHandler(Context context) {
        Log.i(TAG, "串口实例化");
        this.context = context;
        instance = SerialPortUtil.getInstance();
        instance.setOnDataReceiveListener(this);
    }

    /**
     * 实现OnDataReceiveListener接口方法
     *
     * @param buf  字节数组
     * @param size 数组大小
     */
    @Override
    public void onDataReceive(byte[] buf, int size) {
        Log.i(TAG, "onDataReceive()");
        if (buf != null && buf.length > 0 && size > 0) {
            // 将buffer转String
            String readMessage = new String(buf, 0, size);
            buffer.append(readMessage);
            String tempData = buffer.toString();
            // 获取完整的json字符串
            String result = getJsonString(readMessage);
            // 对json结果处理
            if (!TextUtils.isEmpty(result)) {
//                handleJsonResult(result);
            }
        }
    }

    // 发送数据到硬件
    public void sendData(byte[] content) {
        Log.i(TAG, "接收串口Filter");
        // 发送数据到硬件
        if (content != null && content.length > 0) {
            instance.sendBuffer(content);
        }
    }

    //获取完整的json格式数据，可能丢帧
    private String getJsonString(String str) {
        String begin = "{";
        String end = "}";
        String result = "";
        if (!TextUtils.isEmpty(str)) {
            if (str.contains(begin) && str.contains(end)) {
                int start = str.indexOf(begin);
                int stop = str.lastIndexOf(end);
                if (stop > start) {
                    result = str.substring(start, stop + 1);
                    if (!TextUtils.isEmpty(str)) {
                        buffer.delete(start, stop + 1);
                        Log.i(TAG, "getJsonString start===" + start);
                        if (start != 0) {
                            buffer.delete(0, start);
                        }
                    }
                }
            }
        }
        return result;
    }


}
