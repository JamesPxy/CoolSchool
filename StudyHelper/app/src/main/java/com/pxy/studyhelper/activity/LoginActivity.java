package com.pxy.studyhelper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pxy.studyhelper.MyApplication;
import com.pxy.studyhelper.R;
import com.pxy.studyhelper.entity.User;
import com.pxy.studyhelper.utils.LoadingDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import static cn.bmob.v3.BmobUser.getCurrentUser;

@ContentView(value = R.layout.activity_login)
public class LoginActivity extends Activity {

    @ViewInject(value = R.id.username)
    private EditText  edt_username;
    @ViewInject(value = R.id.pwd)
    private EditText  edt_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

    @Event(value = R.id.btn_login,type = View.OnClickListener.class)
    private  void  doLogin(View  v){
//        LoadingDialog.showLoadingDialog(LoginActivity.this);
        String  userName=edt_username.getText().toString().trim();
        String password=edt_pwd.getText().toString().trim();
        if(TextUtils.isEmpty(userName)||
                TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "用户名和密码不能为空...", Toast.LENGTH_SHORT).show();
            return;
        }
        BmobUser bu = new BmobUser();
        bu.setUsername(userName);
        bu.setPassword(password);
        bu.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
//                LoadingDialog.dissmissDialog();
                //给当前用户赋值
                MyApplication.mCurrentUser = getCurrentUser(LoginActivity.this, User.class);
                Toast.makeText(LoginActivity.this, "登录成功...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(LoginActivity.this, "登录失败..." + code + "---" + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Event(value = {R.id.tv_register,R.id.tv_forget_pwd,R.id.tv_see_first},type = View.OnClickListener.class)
    private void  doClick(View view){
        switch (view.getId()){
            case R.id.tv_register:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
            case R.id.tv_forget_pwd:
                Toast.makeText(LoginActivity.this, "有待继续开发...", Toast.LENGTH_SHORT).show();
                LoadingDialog.showLoadingDialog(LoginActivity.this);
//                startActivity(new Intent(LoginActivity.this, TestBigActivity.class));
                break;
            case R.id.tv_see_first:
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                break;
        }
    }




}
