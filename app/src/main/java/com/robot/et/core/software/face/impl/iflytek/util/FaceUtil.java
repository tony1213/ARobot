package com.robot.et.core.software.face.impl.iflytek.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;

public class FaceUtil {
    /**
     * 在指定画布上将人脸框出来
     *
     * @param canvas      给定的画布
     * @param face        需要绘制的人脸信息
     * @param width       原图宽
     * @param height      原图高
     * @param frontCamera 是否为前置摄像头，如为前置摄像头需左右对称
     * @param DrawOriRect 可绘制原始框，也可以只画四个角
     */
    public static void drawFaceRect(Canvas canvas, FaceRect face, int width, int height, boolean frontCamera, boolean DrawOriRect) {
        if (canvas == null) {
            return;
        }

        Paint paint = new Paint();
        paint.setColor(Color.rgb(255, 203, 15));
        int len = (face.bound.bottom - face.bound.top) / 8;
        if (len / 8 >= 2) paint.setStrokeWidth(len / 8);
        else paint.setStrokeWidth(2);

        Rect rect = face.bound;

        if (frontCamera) {
            int top = rect.top;
            rect.top = width - rect.bottom;
            rect.bottom = width - top;
        }

        if (DrawOriRect) {
            paint.setStyle(Style.STROKE);
            canvas.drawRect(rect, paint);
        } else {
            int drawl = rect.left - len;
            int drawr = rect.right + len;
            int drawu = rect.top - len;
            int drawd = rect.bottom + len;

            canvas.drawLine(drawl, drawd, drawl, drawd - len, paint);
            canvas.drawLine(drawl, drawd, drawl + len, drawd, paint);
            canvas.drawLine(drawr, drawd, drawr, drawd - len, paint);
            canvas.drawLine(drawr, drawd, drawr - len, drawd, paint);
            canvas.drawLine(drawl, drawu, drawl, drawu + len, paint);
            canvas.drawLine(drawl, drawu, drawl + len, drawu, paint);
            canvas.drawLine(drawr, drawu, drawr, drawu + len, paint);
            canvas.drawLine(drawr, drawu, drawr - len, drawu, paint);
        }

        if (face.point != null) {
            for (Point p : face.point) {
                if (frontCamera) {
                    p.y = width - p.y;
                }
                canvas.drawPoint(p.x, p.y, paint);
            }
        }
    }

    //获取检测的焦点中心X
    public static float getRectCenterX(FaceRect face) {
        Rect rect = face.bound;
        int left = rect.left;
        int right = rect.right;
        return (right + left) / 2;
    }

    //获取检测的焦点中心Y
    public static float getRectCenterY(FaceRect face) {
        Rect rect = face.bound;
        int top = rect.top;
        int bottom = rect.bottom;
        return (bottom + top) / 2;
    }

    /**
     * 将矩形随原图顺时针旋转90度
     *
     * @param r      待旋转的矩形
     * @param width  输入矩形对应的原图宽
     * @param height 输入矩形对应的原图高
     * @return 旋转后的矩形
     */
    public static Rect RotateDeg90(Rect r, int width, int height) {
        int left = r.left;
        r.left = height - r.bottom;
        r.bottom = r.right;
        r.right = height - r.top;
        r.top = left;
        return r;
    }

    /**
     * 将点随原图顺时针旋转90度
     *
     * @param p      待旋转的点
     * @param width  输入点对应的原图宽
     * @param height 输入点对应的原图宽
     * @return 旋转后的点
     */
    public static Point RotateDeg90(Point p, int width, int height) {
        int x = p.x;
        p.x = height - p.y;
        p.y = x;
        return p;
    }
}
