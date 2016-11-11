package com.robot.et.core.software.slam;

import android.util.Log;

import com.facebook.common.internal.Preconditions;
import com.slamtec.slamware.SlamwareCorePlatform;
import com.slamtec.slamware.action.MoveDirection;
import com.slamtec.slamware.robot.Location;
import com.slamtec.slamware.robot.Pose;
import com.slamtec.slamware.robot.Rotation;

import java.util.List;

/**
 * Created by Tony on 2016/11/11.
 * //光棍节写的，哈哈
 */

public class SlamtecLoader {

    private static final String ROBOT_IP = "192.168.11.1";//slamtec 底盘的IP地址
    private static final int PORT = 1445;//slamtec 访问底盘的端口号

    //Basic MoveDirection
    private static final int MoveDirection_FORWARD = 1;
    private static final int MoveDirection_BACKWARD = 2;
    private static final int MoveDirection_TURN_LEFT = 3;
    private static final int MoveDirection_TURN_RIGHT = 4;

    public SlamwareCorePlatform slamwareCorePlatform;

    private static SlamtecLoader slamtecLoader = null;

    private SlamtecLoader(){

    }

    public static synchronized SlamtecLoader getInstance(){
        if (null == slamtecLoader){
            slamtecLoader = new SlamtecLoader();
        }
        return slamtecLoader;
    }

    //slamtec连接
    public SlamwareCorePlatform execConnect(){
        try {
            slamwareCorePlatform = SlamwareCorePlatform.connect(ROBOT_IP,PORT);
        }catch (Exception e){
            Log.e("slamtec","Connect fail,错误级别高");
            e.printStackTrace();
        }
        return slamwareCorePlatform;
    }

    //slamtec 运动控制
    public void execBasicMove(int direction){
        Preconditions.checkNotNull(slamwareCorePlatform);
        switch (direction){
            case MoveDirection_FORWARD:
                slamwareCorePlatform.moveBy(MoveDirection.FORWARD);
            case MoveDirection_BACKWARD:
                slamwareCorePlatform.moveBy(MoveDirection.BACKWARD);
            case MoveDirection_TURN_LEFT:
                slamwareCorePlatform.moveBy(MoveDirection.TURN_LEFT);
            case MoveDirection_TURN_RIGHT:
                slamwareCorePlatform.moveBy(MoveDirection.TURN_RIGHT);
        }
    }

    //slamtec 控制转向
    public void execBasicRotate(int degree){
        Preconditions.checkNotNull(slamwareCorePlatform);
        slamwareCorePlatform.rotate(new Rotation((float) Math.toRadians((double) degree),0,0));
    }
    
    //TODO slamtec 控制转向的扩展，比如，添加方向参数，参加圈数参数。

    //slamtec获取当前机器人的坐标
    public Location getCurrentRobotPose(){
        Preconditions.checkNotNull(slamwareCorePlatform);
        Pose robotPose = slamwareCorePlatform.getPose();
        Location location = new Location();
        location.setX(robotPose.getX());
        location.setY(robotPose.getY());
        location.setZ(robotPose.getZ());
        return location;
    }

    //slamtec设置导航目标  <策略一>
    public void execSetGoal(Pose pose){
        Preconditions.checkNotNull(slamwareCorePlatform);
        execSetGoal(pose.getLocation());
    }

    //slamtec设置导航目标 <策略二>
    public void execSetGoal(String robotX , String robotY){
        Location location = new Location();
        location.setX(Float.valueOf(robotX));
        location.setY(Float.valueOf(robotY));
        location.setZ(0.0f);
        execSetGoal(location);
    }

    //slamtec设置导航目标 <策略三>
    public void execSetGoal(Location location){
        Preconditions.checkNotNull(slamwareCorePlatform);
        slamwareCorePlatform.moveTo(location);
    }

    //slamtec设置巡逻模式 <策略一>
    public void execPatrol(List<Location> list){
        Preconditions.checkNotNull(slamwareCorePlatform);
        slamwareCorePlatform.moveTo(list);
    }

    //slamtec设置巡逻模式 <策略二>
    public void execPatrol(List<Location> list,boolean appending){
        Preconditions.checkNotNull(slamwareCorePlatform);
        slamwareCorePlatform.moveTo(list,appending);
    }

    //slamtec设置巡逻模式 <策略三>
    public void execPatrol(List<Location> list,boolean appending,boolean isMilestone){
        Preconditions.checkNotNull(slamwareCorePlatform);
        slamwareCorePlatform.moveTo(list,appending,isMilestone);
    }
}
