package com.robot.et.test.slam;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.robot.et.R;
import com.robot.et.core.software.slam.base.BaseActivity;
import com.robot.et.core.software.slam.fragment.MapFragment;
import com.slamtec.slamware.action.MoveDirection;
import com.slamtec.slamware.robot.LaserPoint;
import com.slamtec.slamware.robot.LaserScan;
import com.slamtec.slamware.robot.Location;
import com.slamtec.slamware.robot.Pose;
import com.slamtec.slamware.robot.Rotation;

import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {

    @BindView(R.id.forward)
    Button forward;
    @BindView(R.id.backward) Button backward;
    @BindView(R.id.left) Button left;
    @BindView(R.id.right) Button right;
    @BindView(R.id.clearMap) Button clearMap;
    @BindView(R.id.test) Button test;
    @BindView(R.id.execTurn) Button execTurn;
    @BindView(R.id.moveGoal) Button moveGoal;
    @BindView(R.id.battery)
    TextView battery;
    @BindView(R.id.quality) TextView quality;
    @BindView(R.id.charing) TextView charing;
    @BindView(R.id.angle)
    EditText angle;
    @BindView(R.id.goalx) EditText goalX;
    @BindView(R.id.goaly) EditText goalY;

    static {
        Log.e("MainActivity", "load library");
        System.loadLibrary("rpsdk");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        MapFragment fragment = new MapFragment();
        transaction.add(R.id.container,fragment);
        transaction.commit();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == slamwareCorePlatform) {
            Toast.makeText(MainActivity.this, "连接异常", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.test)
    public void execTest() {
        Log.e("test", "=====================execTest=====================");
        Log.e("RobotStatus", "===================RobotStatus==================");
        Log.e("RobotStatus", "IsCharging:" + slamwareCorePlatform.getBatteryIsCharging());
        Log.e("RobotStatus", "BatteryPercent:" + slamwareCorePlatform.getBatteryPercentage());
        Log.e("RobotStatus", "DCISConnected:" + slamwareCorePlatform.getDCIsConnected());
        Log.e("RobotStatus", "SlamwareVersion" + slamwareCorePlatform.getSlamwareVersion());
        Log.e("RobotStatus", "SDKVersion:" + slamwareCorePlatform.getSDKVersion());
        Log.e("RobotStatus", "================================================");

        Location location = slamwareCorePlatform.getLocation();
        float locationX = location.getX();
        float locationY = location.getY();
        float locationZ = location.getZ();
        Log.e("location", "location:(" + locationX + "," + locationY + "," + locationZ + ")");

        Log.e("mapLocalization", "mapLocalization:" + slamwareCorePlatform.getMapLocalization());
        Log.e("RobotStatus", "===================RobotStatus==================");
        Log.e("RobotStatus", "IsCharging:" + slamwareCorePlatform.getBatteryIsCharging());
        Log.e("RobotStatus", "BatteryPercent:" + slamwareCorePlatform.getBatteryPercentage());
        Log.e("RobotStatus", "DCISConnected:" + slamwareCorePlatform.getDCIsConnected());
        Log.e("RobotStatus", "SlamwareVersion" + slamwareCorePlatform.getSlamwareVersion());
        Log.e("RobotStatus", "SDKVersion:" + slamwareCorePlatform.getSDKVersion());
        Log.e("RobotStatus", "================================================");

        Pose robotPose = slamwareCorePlatform.getPose();
        LaserScan laserScan = slamwareCorePlatform.getLaserScan();
        Vector<LaserPoint> Vpoint = laserScan.getLaserPoints();
        for (int i = 0; i < Vpoint.size(); i++) {
            Log.e("LaserPoint", "Distance:" + Vpoint.get(i).getDistance() + ",angle:" + Vpoint.get(i).getAngle());
        }
    }

    @OnClick(R.id.execTurn)
    public void execTurn(){
        String data=angle.getText().toString().trim();
        if (TextUtils.equals("",data)){
            return;
        }
        double radians=Math.toRadians(Double.valueOf(data));
        Rotation rotation = new Rotation((float)radians,0,0);
        slamwareCorePlatform.rotate(rotation);
    }

    @OnClick(R.id.moveGoal)
    public void execMoveGoal(){
        String dataX = goalX.getText().toString().trim();
        String dataY = goalY.getText().toString().trim();
        if (TextUtils.equals("",dataX) || TextUtils.equals("",dataY)){
            return;
        }
        Location location = new Location();
        location.setX(Float.valueOf(dataX));
        location.setY(Float.valueOf(dataY));
        location.setZ(0.0f);
        slamwareCorePlatform.moveTo(location);
    }
    @OnClick({R.id.forward,R.id.backward,R.id.left,R.id.right})
    public void execForward(View view){
        switch (view.getId()) {
            case R.id.forward:
                slamwareCorePlatform.moveBy(MoveDirection.FORWARD);
                break;
            case R.id.backward:
                slamwareCorePlatform.moveBy(MoveDirection.BACKWARD);
                break;
            case R.id.left:
                slamwareCorePlatform.moveBy(MoveDirection.TURN_LEFT);
                break;
            case R.id.right:
                slamwareCorePlatform.moveBy(MoveDirection.TURN_RIGHT);
                break;
            default:
                break;
        }
    }
}

