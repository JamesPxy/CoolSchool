package com.pxy.studyhelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pxy.studyhelper.MyApplication;
import com.pxy.studyhelper.R;
import com.pxy.studyhelper.utils.SharePreferenceUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * User: Pxy(15602269883@163.com)
 * Date: 2016-02-28
 * Time: 00:57
 * FIXME
 */
@ContentView(value = R.layout.activity_setting)
public class SettingActivity  extends ActivityBase {

    @ViewInject(value = R.id.iv_open_notification)
    private ImageView  iv_open_notification;
    @ViewInject(value = R.id.iv_close_notification)
    private ImageView  iv_close_notification;
    @ViewInject(value = R.id.iv_open_voice)
    private ImageView  iv_open_voice;
    @ViewInject(value = R.id.iv_close_voice)
    private ImageView  iv_close_voice;
    @ViewInject(value = R.id.iv_open_vibrate)
    private ImageView  iv_open_vibrate;
    @ViewInject(value = R.id.iv_close_vibrate)
    private ImageView  iv_close_vibrate;
    @ViewInject(value = R.id.rl_switch_vibrate)
    private RelativeLayout  rl_switch_vibrate;
    @ViewInject(value = R.id.rl_switch_voice)
    private RelativeLayout  rl_switch_voice;
    @ViewInject(value = R.id.view1)
    private View view1;
    @ViewInject(value = R.id.view2)
    private View view2;


    SharePreferenceUtil mSharedUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mSharedUtil = MyApplication.mInstance.getSpUtil();

    }


    @Event(value = {R.id.iv_back,R.id.btn_logout,R.id.layout_info,R.id.rl_switch_notification,R.id.rl_switch_vibrate,R.id.rl_switch_voice},
            type = View.OnClickListener.class)
    private void doCLick(View  view){
        switch (view.getId()){
            case R.id.layout_info:getMyInfo();break;
            case R.id.btn_logout:logOut();
                break;
            case R.id.rl_switch_notification:
                if (iv_open_notification.getVisibility() == View.VISIBLE) {
                    iv_open_notification.setVisibility(View.INVISIBLE);
                    iv_close_notification.setVisibility(View.VISIBLE);
                    mSharedUtil.setPushNotifyEnable(false);
                    rl_switch_vibrate.setVisibility(View.GONE);
                    rl_switch_voice.setVisibility(View.GONE);
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                } else {
                    iv_open_notification.setVisibility(View.VISIBLE);
                    iv_close_notification.setVisibility(View.INVISIBLE);
                    mSharedUtil.setPushNotifyEnable(true);
                    rl_switch_vibrate.setVisibility(View.VISIBLE);
                    rl_switch_voice.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.rl_switch_vibrate:
                if (iv_open_vibrate.getVisibility() == View.VISIBLE) {
                    iv_open_vibrate.setVisibility(View.INVISIBLE);
                    iv_close_vibrate.setVisibility(View.VISIBLE);
                    mSharedUtil.setAllowVibrateEnable(false);
                } else {
                    iv_open_vibrate.setVisibility(View.VISIBLE);
                    iv_close_vibrate.setVisibility(View.INVISIBLE);
                    mSharedUtil.setAllowVibrateEnable(true);
                }
                break;
            case R.id.rl_switch_voice:
                if (iv_open_voice.getVisibility() == View.VISIBLE) {
                    iv_open_voice.setVisibility(View.INVISIBLE);
                    iv_close_voice.setVisibility(View.VISIBLE);
                    mSharedUtil.setAllowVoiceEnable(false);
                } else {
                    iv_open_voice.setVisibility(View.VISIBLE);
                    iv_close_voice.setVisibility(View.INVISIBLE);
                    mSharedUtil.setAllowVoiceEnable(true);
                }

                break;
            case R.id.iv_back:finish();break;
        }
    }

    private void logOut() {
        MyApplication.mInstance.logout();
        finish();
        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
    }

    private void getMyInfo() {
        Intent intent =new Intent(SettingActivity.this,PersonCenterActivity.class);
        intent.putExtra("from",true);
        intent.putExtra("user",MyApplication.mCurrentUser);
        startActivity(intent);
    }

}
