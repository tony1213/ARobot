package com.robot.et.business.voice;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by houdeming on 2016/7/25.
 * 科大讯飞结果json解析
 */
public class UnderstandResultParse {

    private final static String TAG = "json";

    /*
     * 科大讯飞语义理解的json解析
	 * service + question + answer
	 */
    public static void parseAnswerResult(String result, ParseResultCallBack callBack) {
        try {
            JSONTokener tokener = new JSONTokener(result);
            JSONObject jObject = new JSONObject(tokener);
            int isSuccessInt = jObject.getInt("rc");
            String question = jObject.getString("text");
            String service = "";
            // rc=0 操作成功
            if (isSuccessInt == 0) {
                service = jObject.getString("service");
            }
            callBack.getResult(question, service, jObject);
        } catch (JSONException e) {
            Log.i(TAG, "parseIatAnswerResult  JSONException");
            callBack.onError(e.getMessage());
        }
    }

    //百科,计算器,日期,社区问答,褒贬&问候&情绪,闲聊
    public static String getAnswerData(JSONObject jObject) {
        String json = "";
        try {
            JSONObject jsonObject = jObject.getJSONObject("answer");
            json = jsonObject.getString("text");
        } catch (JSONException e) {
            Log.i(TAG, "getAnswerData  JSONException");
        }
        return json;
    }

    //获取菜谱
    public static String getCookBookData(JSONObject jObject) {
        String content = "";
        try {
            JSONObject jsonObject = jObject.getJSONObject("data");
            JSONArray cookArray = jsonObject.getJSONArray("result");
            //菜谱返回的是json数组，默认使用第一个（提高解析效率）备注：第一条数据比较准确。
            JSONObject object = cookArray.getJSONObject(0);
            String ingredient = object.getString("ingredient");// 主要材料
            String accessory = object.getString("accessory");// 辅助材料

            StringBuffer buffer = new StringBuffer(1024);
            buffer.append("主料：").append(ingredient);
            if (!TextUtils.isEmpty(accessory)) {
                buffer.append("，辅料：").append(accessory);
            }
            content = buffer.toString();

        } catch (JSONException e) {
            Log.i(TAG, "getCookBookData  JSONException");
        }
        return content;
    }

    //获取音乐
    public static String getMusicData(JSONObject jObject) {
        String json = "";
        try {
            JSONObject jsonObject = jObject.getJSONObject("data");
            JSONArray musicArray = jsonObject.getJSONArray("result");
            List<String> musics = new ArrayList<String>();
            for (int i = 0; i < musicArray.length(); i++) {
                JSONObject object = musicArray.getJSONObject(i);
                String url = object.getString("downloadUrl");// 音乐地址
                String singer = "";
                if (object.has("singer")) {
                    singer = object.getString("singer");//歌手
                }
                String musicName = "";
                if (object.has("name")) {
                    musicName = object.getString("name");//歌曲
                }
                if (!TextUtils.isEmpty(url)) {
                    StringBuffer buffer = new StringBuffer(1024);
                    String musicSplit = "&";
                    buffer.append(singer).append(musicSplit).append(musicName).append(musicSplit).append(url);
                    //歌手+歌名 + 歌曲src
                    musics.add(buffer.toString());
                }
            }

            int size = musics.size();
            if (musics != null && size > 0) {
                Random random = new Random();
                int randNum = random.nextInt(size);
                json = musics.get(randNum);
            }
        } catch (JSONException e) {
            Log.i(TAG, "getMusicData  JSONException");
        }
        return json;
    }

    //获取提醒
    public static String getRemindData(JSONObject jObject) {
        String result = "";
        try {
            JSONObject jsonObject = jObject.getJSONObject("semantic");
            JSONObject object = jsonObject.getJSONObject("slots");
            String sign = "&";
            StringBuffer buffer = new StringBuffer(1024);
            if (object.has("datetime")) {
                JSONObject dataObject = object.getJSONObject("datetime");
                String date = dataObject.getString("date");// 日期
                buffer.append(date).append(sign);
                String time = dataObject.getString("time");// 时间
                buffer.append(time).append(sign);
                String dateOrig = "";
                if (dataObject.has("dateOrig")) {
                    dateOrig = dataObject.getString("dateOrig");// 说的日期
                }
                buffer.append(dateOrig).append(sign);
                String timeOrig = "";
                if (dataObject.has("timeOrig")) {
                    timeOrig = dataObject.getString("timeOrig");// 说的时间
                }
                buffer.append(timeOrig).append(sign);
            }
            String content = object.getString("content");// 做什么事
            buffer.append(content);
            result = buffer.toString();
        } catch (JSONException e) {
            Log.i(TAG, "getRemindData  JSONException");
        }
        return result;
    }

    //获取天气
    public static String getWeatherData(JSONObject jObject, String city, String area) {
        String content = "";
        try {
            JSONObject object1 = jObject.getJSONObject("semantic");
            JSONObject object2 = object1.getJSONObject("slots");
            JSONObject object3 = object2.getJSONObject("datetime");
            String time = "";
            if (object3.has("dateOrig")) {
                time = object3.getString("dateOrig");// 日期
            } else {
                time = "今天";
            }
            JSONObject object4 = object2.getJSONObject("location");
            String iflyCity = object4.getString("city");// 城市
            String iflyArea = "";
            if (object4.has("area")) {// 区域
                iflyArea = object4.getString("area");// 区域
            }

            JSONObject jsonObject = jObject.getJSONObject("data");
            JSONArray dataArray = jsonObject.getJSONArray("result");

            //天气返回的是json数组，默认使用第一个（提高解析效率）备注：第一条数据字段是最全面的。
            JSONObject object = dataArray.getJSONObject(0);
            String airQuality = "";
            if (object.has("airQuality")) {
                airQuality = object.getString("airQuality");// 空气质量
            }
            String wind = object.getString("wind");// 风向以及风力
            String weather = object.getString("weather");// 天气现象
            String tempRange = object.getString("tempRange");// 气温范围25℃~19℃
            String pmValue = "";
            if (object.has("pm25")) {
                pmValue = object.getString("pm25");// 空气质量
            }

            StringBuffer buffer = new StringBuffer(1024);
            if (city.contains(iflyCity)) {// 是当前城市
                if (!TextUtils.isEmpty(iflyArea)) {
                    buffer.append(time).append(iflyCity).append(iflyArea);
                } else {
                    buffer.append(time).append(iflyCity).append(area);
                }
            } else {// 不是当前城市
                if (!TextUtils.isEmpty(iflyArea)) {
                    buffer.append(time).append(iflyCity).append(iflyArea);
                } else {
                    if (TextUtils.equals(iflyCity, "CURRENT_CITY")) {
                        content = buffer.append(time).append(city).append("的天气").toString();
                        return content;
                    } else {
                        buffer.append(time).append(iflyCity);
                    }

                }
            }

            if (TextUtils.isEmpty(airQuality)) {
                buffer.append("天气：").append(weather).append(",风力：").append(wind).append(",气温：").append(tempRange);
            } else {
                buffer.append("天气：").append(weather).append(",空气质量：").append(airQuality).append(",风力：").append(wind).append(",气温：").append(tempRange);
            }

            if (!TextUtils.isEmpty(pmValue)) {
                buffer.append(",pm2.5：").append(pmValue);
            }

            content = buffer.toString();
        } catch (JSONException e) {
            Log.i(TAG, "getWeatherData  JSONException");
        }
        return content;
    }

    //获取打电话
    public static String getPhoneData(JSONObject jObject) {
        String json = "";
        try {
            JSONObject jsonObject = jObject.getJSONObject("semantic");
            JSONObject object = jsonObject.getJSONObject("slots");
            if (object.has("name")) {
                json = object.getString("name");// 拨打电话的人名
            } else if (object.has("code")) {
                json = object.getString("code");// 拨打电话的号码
            }
        } catch (JSONException e) {
            Log.i(TAG, "getPhoneData  JSONException");
        }
        return json;
    }

    //获取空气质量
    public static String getPm25Data(JSONObject jObject, String city, String area) {
        String content = "";
        try {
            JSONObject json1 = jObject.getJSONObject("semantic");
            JSONObject json2 = json1.getJSONObject("slots");
            JSONObject json3 = json2.getJSONObject("location");
            String iflyCity = json3.getString("city");// 城市
            String iflyArea = "";
            if (json3.has("area")) {
                iflyArea = json3.getString("area");// 区域
            }

            JSONObject jsonObject = jObject.getJSONObject("data");
            JSONArray dataArray = jsonObject.getJSONArray("result");

            //空气质量返回的是json数组，默认使用第一个（提高解析效率）备注：第一条数据比较准确。
            JSONObject object = dataArray.getJSONObject(0);
            String pmValue = object.getString("pm25");// pm值
            String weather = object.getString("quality");// 空气质量
            String aqi = object.getString("aqi");// 空气质量指数

            StringBuffer buffer = new StringBuffer(1024);
            if (city.contains(iflyCity)) {// 是当前城市
                if (!TextUtils.isEmpty(iflyArea)) {// 有返回区
                    buffer.append(iflyCity).append(iflyArea);
                } else {// 没有返回区
                    buffer.append(iflyCity).append(area);
                }
            } else {// 不是当前城市
                if (!TextUtils.isEmpty(iflyArea)) {// 有返回区
                    buffer.append(iflyCity).append(iflyArea);
                } else {// 没有返回区
                    if (TextUtils.equals(iflyCity, "CURRENT_CITY")) {
                        content = buffer.append(city).append(area).append("的pm2.5").toString();
                        return content;
                    } else {
                        buffer.append(iflyCity);
                    }
                }
            }

            buffer.append("pm2.5：").append(pmValue).append(",空气质量：").append(weather).append(",空气质量指数：").append(aqi);
            content = buffer.toString();

        } catch (JSONException e) {
            Log.i(TAG, "getPm25Data  JSONException");
        }
        return content;
    }

    //获取电台
    public static String getRadioName(JSONObject jObject) {
        String json = "";
        try {
            JSONObject jsonObject = jObject.getJSONObject("semantic");
            JSONObject object = jsonObject.getJSONObject("slots");
            if (object.has("name")) {
                json = object.getString("name");// 电台名字
            }
        } catch (JSONException e) {
            Log.i(TAG, "getRadioName  JSONException");
        }
        return json;
    }
}