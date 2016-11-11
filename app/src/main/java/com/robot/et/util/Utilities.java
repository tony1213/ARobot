package com.robot.et.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * Created by houdeming on 2016/8/4.
 */
public class Utilities {

    private static final String TAG = "Utilities";

    /**
     * 是否连接网络
     * @param context Android上下文
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 中文的数字转int
     * @param str
     * @return
     */
    public static int chineseNum2Int(String str) {
        int result = 0;
        if (!TextUtils.isEmpty(str)) {
            int temp = 1;// 存放一个单位的数字如：十万
            int count = 0;// 判断是否有chArr
            int strNum = 0;// 是否有数字
            char[] cnArr = new char[]{'一', '二', '三', '四', '五', '六', '七', '八', '九', '两'};
            char[] chArr = new char[]{'十', '百', '千', '万', '亿'};
            int length = str.length();
            int cnArrLength = cnArr.length;
            int chArrLength = chArr.length;
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < length; i++) {
                char c = str.charAt(i);
                if (Character.isDigit(c)) {// 是数字
                    buffer.append(c);
                } else {
                    boolean b = true;// 判断是否是chArr
                    for (int j = 0; j < cnArrLength; j++) {// 非单位，即数字
                        if (c == cnArr[j]) {
                            if (0 != count) {// 添加下一个单位之前，先把上一个单位值添加到结果中
                                result += temp;
                                count = 0;
                            }
                            // 下标+1，就是对应的值
                            if (j == cnArrLength - 1) {
                                temp = 2;
                            } else {
                                temp = j + 1;
                            }
                            b = false;
                            strNum = 1;
                            break;
                        }
                    }
                    if (b) {// 单位{'十','百','千','万','亿'}
                        for (int j = 0; j < chArrLength; j++) {
                            if (c == chArr[j]) {
                                switch (j) {
                                    case 0:
                                        temp *= 10;
                                        break;
                                    case 1:
                                        temp *= 100;
                                        break;
                                    case 2:
                                        temp *= 1000;
                                        break;
                                    case 3:
                                        temp *= 10000;
                                        break;
                                    case 4:
                                        temp *= 100000000;
                                        break;
                                    default:
                                        break;
                                }
                                count++;
                                strNum = 1;
                            }
                        }
                    }
                    if (i == str.length() - 1) {// 遍历到最后一个字符
                        if (strNum != 0) {
                            result += temp;
                        }
                    }
                }
            }
            String tempDistance = buffer.toString();
            if (!TextUtils.isEmpty(tempDistance)) {
                if (TextUtils.isDigitsOnly(tempDistance)) {
                    result = Integer.parseInt(tempDistance);
                }
            }
        }
        return result;
    }
}
