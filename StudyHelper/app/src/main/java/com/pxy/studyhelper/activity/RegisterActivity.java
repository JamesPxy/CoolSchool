package com.pxy.studyhelper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pxy.studyhelper.R;
import com.pxy.studyhelper.entity.User;
import com.pxy.studyhelper.utils.Tools4Sure;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.bmob.v3.listener.SaveListener;


@ContentView(value = R.layout.activity_register)
public class RegisterActivity extends Activity {

    @ViewInject(value = R.id.phone_layout)
    private LinearLayout  phone_layout;
    @ViewInject(value = R.id.mail_layout)
    private LinearLayout mail_layout;
    @ViewInject(value = R.id.regUsr)
    private EditText edt_user;
    @ViewInject(value = R.id.regPwd)
    private EditText edt_pwd;
    @ViewInject(value = R.id.imglabel)
    private ImageView  mImageLabel;
    @ViewInject(value = R.id.hidePwdImg)
    private ImageView  mImgShowPwd;

    private boolean  isEmail=false;
    private boolean  isShowPwd=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

    @Event(value = {R.id.phone_layout,R.id.mail_layout},type=View.OnClickListener.class)
    private void  doChoice(View view){
        switch (view.getId()){
            case R.id.phone_layout:
                isEmail=false;
                mImageLabel.setImageResource(R.drawable.reg_phone_icon);
                edt_user.setHint("请输入手机号");
                edt_user.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);
                phone_layout.setBackgroundResource(R.drawable.sso_reg_topleft_bg_press);
                mail_layout.setBackgroundResource(R.drawable.sso_reg_topright_bg_normal);
                break;
            case R.id.mail_layout:isEmail=true;
                mImageLabel.setImageResource(R.drawable.reg_mail_icon);
                edt_user.setHint("请输入邮箱");
                edt_user.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                mail_layout.setBackgroundResource(R.drawable.sso_reg_topright_bg_press);
                phone_layout.setBackgroundResource(R.drawable.sso_reg_topleft_bg_normal);
                break;
        }
    }

    @Event(value = R.id.btn_register,type = View.OnClickListener.class)
    private void doRegist(View view){
        User user = new User();
        final String  userName=edt_user.getText().toString().trim();
        final String password=edt_pwd.getText().toString().trim();

        if(TextUtils.isEmpty(password)||TextUtils.isEmpty(userName)){
            Toast.makeText(RegisterActivity.this, "密码不能为空...", Toast.LENGTH_SHORT).show();
            return;
        }
        //  校验  手机号 邮箱
        if(isEmail){
            if(!Tools4Sure.isValidEmail(userName)){
                edt_user.setError("邮箱格式错误...");
                return;
            }
            user.setEmail(userName);
        }else{
            if(!Tools4Sure.isRightMobilePhoe(userName)){
                edt_user.setError("手机号格式错误...");
                return;
            }
            user.setMobilePhoneNumber(userName);
        }
        user.setUsername(userName);
        user.setPassword(password);
        user.setSex(false);
        user.setLevel(0);
        user.setScore(0);
        user.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegisterActivity.this, "恭喜你,注册成功...", Toast.LENGTH_LONG).show();
                Intent  intent=new Intent(RegisterActivity.this,LoginActivity.class);
                intent.putExtra("name",userName);
                intent.putExtra("password",password);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(RegisterActivity.this, "注册失败:" + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Event(value = R.id.hidePwdImg,type = View.OnClickListener.class)
    private void  showPwd(View view){
        String password=edt_pwd.getText().toString().trim();
        if(isShowPwd){
            mImgShowPwd.setImageResource(R.drawable.sso_showpwd);
            edt_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        }else{
            mImgShowPwd.setImageResource(R.drawable.sso_hidepwd);
            edt_pwd.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        isShowPwd=!isShowPwd;
        edt_pwd.setSelection(password.length());
    }

    @Event(value = R.id.img_back,type = View.OnClickListener.class)
    private void doBack(View view){
        finish();
    }
}
