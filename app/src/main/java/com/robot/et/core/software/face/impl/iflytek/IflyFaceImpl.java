package com.robot.et.core.software.face.impl.iflytek;

import android.content.Context;
import android.content.Intent;

import com.robot.et.core.software.face.IFace;
import com.robot.et.core.software.face.callback.FaceCallBack;
import com.robot.et.entity.FaceInfo;

import java.util.ArrayList;

/**
 * Created by houdeming on 2016/11/1.
 */
public class IflyFaceImpl implements IFace {

    private Context context;
    private static FaceCallBack callBack;

    public IflyFaceImpl(Context context) {
        this.context = context;
    }

    protected static FaceCallBack getCallBack() {
        return callBack;
    }

    @Override
    public void openFaceDistinguish(boolean isVoiceOpen, ArrayList<FaceInfo> infos, FaceCallBack callBack) {
        this.callBack = callBack;
        Intent intent = new Intent(context, IflyFaceDistinguishActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("FaceInfo", infos);
        context.startActivity(intent);
    }

    @Override
    public boolean isFaceDistinguish() {
        if (IflyFaceDistinguishActivity.instance != null) {
            return true;
        }
        return false;
    }

    @Override
    public void closeFaceDistinguish() {
        if (IflyFaceDistinguishActivity.instance != null) {
            IflyFaceDistinguishActivity.instance.finish();
            IflyFaceDistinguishActivity.instance = null;
        }
    }
}
