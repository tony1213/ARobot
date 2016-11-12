package com.robot.et.base;

import android.app.Fragment;
import android.os.Bundle;

import com.robot.et.app.CustomApplication;
import com.robot.et.core.software.slam.SlamtecLoader;
import com.slamtec.slamware.SlamwareCorePlatform;

/**
 * Created by Tony on 2016/11/2.
 */

public class BaseFragment extends Fragment {

    public SlamwareCorePlatform slamwareCorePlatform;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSlam();
    }
    private void initSlam(){
        slamwareCorePlatform = SlamtecLoader.getInstance().execConnect();
    }
}
