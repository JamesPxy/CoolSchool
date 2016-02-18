package com.pxy.studyhelper.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.pxy.studyhelper.R;
import com.pxy.studyhelper.utils.Tools4Sure;
import com.pxy.studyhelper.entity.User;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.bmob.v3.listener.SaveListener;


@ContentView(value = R.layout.activity_register)
public class RegisterActivity extends Activity {

    @ViewInject(value = R.id.rbt_phone)
    private RadioButton rbt_phone;
    @ViewInject(value = R.id.rbt_email)
    private RadioButton rbt_email;
    @ViewInject(value = R.id.regUsr)
    private EditText edt_user;
    @ViewInject(value = R.id.regPwd)
    private EditText edt_pwd;
    @ViewInject(value = R.id.imglabel)
    private ImageView  mImageLabel;
    @ViewInject(value = R.id.radioGroup)
    private RadioGroup  mRadioGroup;
    @ViewInject(value = R.id.hidePwdImg)
    private ImageView  mImgShowPwd;

    private boolean  isEmail=false;
    private boolean  isShowPwd=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(rbt_email.isChecked()){
                    isEmail=true;
                    mImageLabel.setImageResource(R.drawable.reg_mail_icon);
                    edt_user.setHint("请输入邮箱");
                    edt_user.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                }else{
                    isEmail=false;
                    mImageLabel.setImageResource(R.drawable.reg_phone_icon);
                    edt_user.setHint("请输入手机号");
                    edt_user.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);
                }
            }
        });

    }

    @Event(value = R.id.btn_register,type = View.OnClickListener.class)
    private void doRegist(View view){
        User user = new User();
        String  userName=edt_user.getText().toString().trim();
        String password=edt_pwd.getText().toString().trim();

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
        user.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegisterActivity.this, "恭喜你,注册成功...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(RegisterActivity.this, "注册失败..." + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Event(value = R.id.hidePwdImg,type = View.OnClickListener.class)
    private void  showPwd(View view){
        if(isShowPwd){
            mImgShowPwd.setImageResource(R.drawable.sso_showpwd);
            isShowPwd=false;
            edt_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        }else{
            mImgShowPwd.setImageResource(R.drawable.sso_hidepwd);
            isShowPwd=true;
            edt_pwd.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    @Event(value = R.id.img_back,type = View.OnClickListener.class)
    private void doBack(View view){
        finish();
    }
}
