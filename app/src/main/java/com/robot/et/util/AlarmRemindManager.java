package com.robot.et.util;

import android.text.TextUtils;
import android.util.Log;

import com.robot.et.core.software.system.AlarmClock;
import com.robot.et.db.RobotDB;
import com.robot.et.entity.RemindInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 闹钟提醒管理
 */
public class AlarmRemindManager {
    private static final String ALARM_TAG = "alarm";
    // 闹钟提醒的标志
    public static final String ALARM_SIGN = "&ALARM&";
    //已提醒
    public static final int REMIND_HAD_ID = 1;
    //未提醒
    public static final int REMIND_NO_ID = 0;
    //设置为每天的闹铃
    public static final int ALARM_ALL_DAY = 0;

    /**
     * 设置闹铃
     *
     * @param date 日期 yyyy-MM-dd
     * @param time 时间 HH:mm:ss
     */
    public static void setAlarmClock(String date, String time) {
        Calendar calendar = DateTools.getCalendar(date, time);
        long currentMinute = System.currentTimeMillis();
        StringBuffer buffer = new StringBuffer(1024);
        String action = buffer.append(DateTools.getCurrentDate(currentMinute)).append(ALARM_SIGN).append(DateTools.getCurrentTime(currentMinute)).toString();
        AlarmClock.getInstance().setOneAlarm(action, calendar);
    }

    /**
     * 设置提醒
     *
     * @param minute 时间
     */
    public static void setAlarmClock(long minute) {
        String tempTime = DateTools.getCurrentTimeDetail(minute);
        String[] times = tempTime.split(" ");
        String date = times[0];
        String time = times[1];
        Log.i(ALARM_TAG, "date===" + date);
        Log.i(ALARM_TAG, "time===" + time);
        setAlarmClock(date, time);
    }

    /**
     * 设置闹铃时间的格式 HH:mm:ss--->>转 HH:mm:00
     *
     * @param time 时间
     * @return
     */
    private static String setAlarmTimeFormat(String time) {
        String result = "";
        if (!TextUtils.isEmpty(time)) {
            result = time.substring(0, time.length() - 2) + "00";
        }
        return result;
    }

    /**
     * 获取提醒的内容
     *
     * @param minute 时间
     * @return
     */
    public static List<RemindInfo> getRemindTips(long minute) {
        String date = DateTools.getCurrentDate(minute);
        int currentHour = DateTools.getCurrentHour(minute);
        String minuteTwo = DateTools.get2DigitMinute(minute);
        String time = new StringBuffer(1024).append(currentHour).append(":").append(minuteTwo).append(":").append("00").toString();
        RobotDB mDao = RobotDB.getInstance();
        List<RemindInfo> infos = new ArrayList<RemindInfo>();
        try {
            infos = mDao.getRemindInfos(date, time, REMIND_NO_ID);
        } catch (Exception e) {
            Log.i(ALARM_TAG, "getRemindTips() Exception===" + e.getMessage());
        }
        return infos;
    }

    /**
     * 更新已经提醒的条目
     * @param info 提醒的信息
     * @param minute 时间
     * @param frequency 频次
     */
    public static void updateRemindInfo(RemindInfo info, long minute, int frequency) {
        String date = DateTools.getCurrentDate(minute);
        RobotDB.getInstance().updateRemindInfo(info, date, frequency);
    }

    /**
     * 删除当前时间提醒的条目
     * @param minute 时间
     */
    public static void deleteRemindTips(long minute) {
        String date = DateTools.getCurrentDate(minute);
        int currentHour = DateTools.getCurrentHour(minute);
        String currentMinute = DateTools.get2DigitMinute(minute);
        String time = new StringBuffer(1024).append(currentHour).append(":").append(currentMinute).append(":").append("00").toString();
        RobotDB.getInstance().deleteRemindInfo(date, time, REMIND_NO_ID);
    }

    /**
     * 删除传来的提醒
     * @param originalTime 发过来的的提醒时间
     */
    public static void deleteRemindTips(String originalTime) {
        if (!TextUtils.isEmpty(originalTime)) {
            RobotDB.getInstance().deleteAppRemindInfo(originalTime);
        }
    }

    /**
     * 增加语音提醒的操作 格式：日期 + 时间 + 说的日期 + 说的时间 + 做什么事
     * @param remindInfo 提醒的信息
     * @return
     */
    private static boolean addVoiceRemind(RemindInfo remindInfo) {
        if (remindInfo != null) {
            remindInfo.setRemindInt(REMIND_NO_ID);
            remindInfo.setFrequency(1);
            boolean flag = addAlarm(remindInfo);
            if (flag) {
                setAlarmClock(remindInfo.getDate(), remindInfo.getTime());
                return flag;
            }
        }
        return false;
    }

    /**
     * 获取提醒的info
     * @param robotNum 机器编号
     * @param data 日期
     * @param time 时间
     * @param content 内容
     * @return
     */
    public static RemindInfo getRemindInfo(String robotNum, String data, String time, String content) {
        RemindInfo info = new RemindInfo();
        info.setRobotNum(robotNum);
        info.setDate(data);
        info.setTime(setAlarmTimeFormat(time));
        info.setContent(content);
        return info;
    }

    /**
     * 获取语音提醒的提示语
     * @param info
     * @return
     */
    public static String getVoiceRemindTips(RemindInfo info) {
        boolean flag = addVoiceRemind(info);
        String content = "";
        if (flag) {
            // 格式：好的，明天xx时间提醒xx
            StringBuffer buffer = new StringBuffer();
            buffer.append("好的，");
            String speakDate = info.getSpeakDate();//要说的日期
            if (!TextUtils.isEmpty(speakDate)) {
                buffer.append(speakDate);
            }
            String speakTime = info.getSpeakTime();//要说的时间
            if (!TextUtils.isEmpty(speakTime)) {
                buffer.append(speakTime);
            }
            buffer.append("提醒");
            buffer.append(info.getContent());
            content = buffer.toString();
        } else {
            content = "主人，这个提醒我已经记住了呢，不用重复提醒哦";
        }
        return content;
    }

    /**
     * 获取多个闹铃提示
     * @return
     */
    public static String getMoreAlarmContent() {
        String content = "";
        List<String> datas = getAlarmDatas();
        int size = datas.size();
        Log.i(ALARM_TAG, "datas.size====" + size);
        if (datas != null && size > 0) {
            content = "主人，要" + datas.get(size - 1) + "了";
            datas.remove(size - 1);
            setAlarmDatas(datas);
        }
        // 设置单次闹铃
        setSpeakRemindContent(content);
        return content;
    }

    /**
     * 增加闹铃到数据库
     * @param info
     * @return
     */
    private static boolean addAlarm(RemindInfo info) {
        RobotDB mDb = RobotDB.getInstance();
        RemindInfo mInfo = mDb.getRemindInfo(info);
        if (mInfo != null) {
            Log.i(ALARM_TAG, "闹铃已存在");
            return false;
        }
        mDb.addRemindInfo(info);
        Log.i(ALARM_TAG, "增加闹铃成功");
        return true;
    }

    //多次提醒的数据
    private static List<String> alarmDatas = new ArrayList<String>();
    private static String speakRemindContent;

    public static List<String> getAlarmDatas() {
        return alarmDatas;
    }

    public static void setAlarmDatas(List<String> alarmDatas) {
        AlarmRemindManager.alarmDatas = alarmDatas;
    }

    public static String getSpeakRemindContent() {
        return speakRemindContent;
    }

    public static void setSpeakRemindContent(String speakRemindContent) {
        AlarmRemindManager.speakRemindContent = speakRemindContent;
    }
}
