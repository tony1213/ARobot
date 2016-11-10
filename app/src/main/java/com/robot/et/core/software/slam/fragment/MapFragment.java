package com.robot.et.core.software.slam.fragment;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.robot.et.R;
import com.robot.et.core.software.slam.base.BaseFragment;
import com.robot.et.core.software.slam.view.ScaleImageView;
import com.slamtec.slamware.robot.Map;
import com.slamtec.slamware.robot.MapKind;
import com.slamtec.slamware.robot.MapType;
import com.slamtec.slamware.robot.Pose;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BaseFragment {

    private View view;
    private ScaleImageView mapView;
    private Map map;
    private Bitmap bitmap;

    private static final int UPATE_MAP_SIGN = 17;


    public MapFragment() {
        // Required empty public constructor
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPATE_MAP_SIGN) {
                showMap();
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (ScaleImageView) view.findViewById(R.id.imageView1);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null == slamwareCorePlatform) {
            Toast.makeText(getActivity(), "连接异常", Toast.LENGTH_LONG).show();
        } else {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = UPATE_MAP_SIGN;
                    handler.sendMessage(msg);
                }
            }, 1000, 1000);
        }
    }

    private void showMap() {
        RectF knowArea = slamwareCorePlatform.getKnownArea(MapType.BITMAP_8BIT, MapKind.EXPLORE_MAP);
        map = slamwareCorePlatform.getMap(MapType.BITMAP_8BIT, MapKind.EXPLORE_MAP, knowArea);
        bitmap = Bitmap.createBitmap(map.getDimension().getWidth() + 1, map.getDimension().getHeight() + 1, Bitmap.Config.ARGB_4444);
        for (int posY = 0; posY < map.getDimension().getHeight(); ++posY) {
            for (int posX = 0; posX < map.getDimension().getWidth(); ++posX) {
                int color = 127 + map.getData()[posX + (map.getDimension().getHeight() - posY - 1) * map.getDimension().getWidth()];
                bitmap.setPixel(posX, posY, Color.rgb(color, color, color));
            }
        }
        mapView.setImageBitmap(bitmap);
        mapView.invalidate();
    }

    private void displayLocalizationInfo(Pose robotPose) {
        Log.e("Pose", "-----------RobotPose--------------");
        Log.e("Pose", "poseX:" + robotPose.getX());
        Log.e("Pose", "poseY:" + robotPose.getY());
        Log.e("Pose", "poseZ:" + robotPose.getZ());
        Log.e("Pose", "yaw:" + robotPose.getYaw());
        Log.e("Pose", "pitch:" + robotPose.getPitch());
        Log.e("Pose", "roll:" + robotPose.getRoll());
        Log.e("Pose", "==================================");
    }
}
