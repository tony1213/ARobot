package com.robot.et.core.software.face.impl.iflytek.util;

import android.graphics.Point;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Iterator;

public class FaceParseResult {
    /**
     * 离线人脸框结果解析方法
     *
     * @param json
     * @return
     */
    public static FaceRect[] parseResult(String json) {
        FaceRect[] rect = null;
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);
            // 获取每个人脸的结果
            JSONArray items = joResult.getJSONArray("face");
            // 获取人脸数目
            rect = new FaceRect[items.length()];
            for (int i = 0; i < items.length(); i++) {

                JSONObject position = items.getJSONObject(i).getJSONObject("position");
                // 提取关键点数据
                rect[i] = new FaceRect();
                rect[i].bound.left = position.getInt("left");
                rect[i].bound.top = position.getInt("top");
                rect[i].bound.right = position.getInt("right");
                rect[i].bound.bottom = position.getInt("bottom");

                try {
                    JSONObject landmark = items.getJSONObject(i).getJSONObject("landmark");
                    int keyPoint = landmark.length();
                    rect[i].point = new Point[keyPoint];
                    Iterator it = landmark.keys();
                    int point = 0;
                    while (it.hasNext() && point < keyPoint) {
                        String key = (String) it.next();
                        JSONObject postion = landmark.getJSONObject(key);
                        rect[i].point[point] = new Point(postion.getInt("x"), postion.getInt("y"));
                        point++;
                    }
                } catch (JSONException e) {
                    Log.i("face", "parseResult JSONException==" + e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.i("face", "parseResult Exception==" + e.getMessage());
        }
        return rect;
    }
}