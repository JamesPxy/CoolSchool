package com.pxy.studyhelper.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.pxy.studyhelper.MyApplication;
import com.pxy.studyhelper.R;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by pxy on 2016-02-24.
 */

@ContentView(value = R.layout.activity_welcome)
public class WelcomeActivity  extends AppCompatActivity {

    @ViewInject(value = R.id.tv_version)
    private TextView  version;
    Handler mHandler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        //设置版本号
        try {
            int  code=getPackageManager().getPackageInfo(getPackageName(),0).versionCode;
            String  s=getPackageManager().getPackageInfo(getPackageName(),0).versionName;
//            String  s=Tools.getCurrentVersion(this);
            version.setText("Version:"+s);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e("error--" + e.getMessage());
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doStart();
            }
        }, 2000);
    }

    private void doStart() {
        Intent intent;
        if(MyApplication.mCurrentUser!=null){
            //缓存对象不为空时允许用户使用
            intent=new Intent(this, MainActivity.class);
        }else{
            intent=new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
