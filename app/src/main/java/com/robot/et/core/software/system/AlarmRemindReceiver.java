package com.robot.et.core.software.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.robot.et.entity.RemindInfo;
import com.robot.et.util.AlarmRemindManager;

import java.util.ArrayList;
import java.util.List;

// 闹铃接受器
public class AlarmRemindReceiver extends BroadcastReceiver {

    private static final String ALARM_TAG = "alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!TextUtils.isEmpty(action)) {
            // 只有是特定的广播才接受，做出相应   有提醒时，以时间格式发出的广播，这里只接受闹铃提醒的广播
            if (action.contains(AlarmRemindManager.ALARM_SIGN)) {
                Log.i(ALARM_TAG, "接受到提醒的广播");
                AlarmClock.getInstance().cancelOneAlarm(action);
                remindTips();
            }
        }
    }

    //闹铃提醒
    private void remindTips() {
        long minute = System.currentTimeMillis();
        // 获取当前时间闹铃提醒的信息
        List<RemindInfo> infos = AlarmRemindManager.getRemindTips(minute);
        int infoSize = infos.size();
        Log.i(ALARM_TAG, "infoSize.size()===" + infoSize);
        // 可能同一时间有多个闹铃，遍历多个闹铃
        if (infos != null && infoSize > 0) {
            List<String> datas = new ArrayList<String>();
            for (int i = 0; i < infoSize; i++) {
                //更新已经提醒过的内容
                RemindInfo info = infos.get(i);
                Log.i(ALARM_TAG, "info.getContent()===" + info.getContent());
                datas.add(info.getContent());

                int frequency = info.getFrequency();
                if (frequency == AlarmRemindManager.ALARM_ALL_DAY) {//每天
                    if (!TextUtils.isEmpty(info.getRemindMen())) {
                        //app提醒
                        Log.i(ALARM_TAG, "app提醒");
                        AlarmRemindManager.deleteRemindTips(info.getOriginalAlarmTime());
                    } else {
                        //APP设置的闹铃
                        Log.i(ALARM_TAG, "APP设置的闹铃");
                        minute += 24 * 60 * 60 * 1000;
                        AlarmRemindManager.updateRemindInfo(info, minute, AlarmRemindManager.ALARM_ALL_DAY);
                        AlarmRemindManager.addAlarmClock(minute);
                    }

                } else {//不是每天
                    if (frequency == 1) {
                        AlarmRemindManager.deleteRemindTips(minute);
                    } else {
                        minute += 24 * 60 * 60 * 1000;
                        AlarmRemindManager.updateRemindInfo(info, minute, frequency - 1);
                        AlarmRemindManager.addAlarmClock(minute);
                    }
                }
            }

            int size = datas.size();
            Log.i(ALARM_TAG, "闹铃size===" + size);
            if (datas != null && size > 0) {
                if (size > 1) {//多个闹铃
                    datas.remove(size - 1);
                    AlarmRemindManager.setAlarmDatas(datas);
                }
            }

            RemindInfo info = infos.get(infos.size() - 1);
            String remindContent = info.getContent();
            String content = "";

            //闹铃
            content = "主人，要" + remindContent + "了";
            Log.i(ALARM_TAG, "content===" + content);
        }
    }
}
