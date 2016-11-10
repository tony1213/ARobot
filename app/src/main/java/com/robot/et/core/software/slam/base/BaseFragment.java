package com.robot.et.core.software.slam.base;

import android.app.Fragment;
import android.os.Bundle;

import com.robot.et.app.CustomApplication;
import com.slamtec.slamware.SlamwareCorePlatform;

/**
 * Created by Tony on 2016/11/2.
 */

public class BaseFragment extends Fragment {

    private CustomApplication application;
    public SlamwareCorePlatform slamwareCorePlatform;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (CustomApplication)getActivity().getApplication();
        slamwareCorePlatform = application.getSlamwareCorePlatform();
    }
}
