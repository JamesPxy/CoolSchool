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
import com.pxy.studyhelper.utils.CollectionUtils;
import com.pxy.studyhelper.utils.LoadingDialog;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.v3.BmobUser.getCurrentUser;

@ContentView(value = R.layout.activity_login)
public class LoginActivity extends Activity {

    @ViewInject(value = R.id.username)
    private EditText  edt_username;
    @ViewInject(value = R.id.pwd)
    private EditText  edt_pwd;

    String  userName;
    String  password;
    BmobUserManager userManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        userName=getIntent().getStringExtra("name");
        password=getIntent().getStringExtra("password");
        userManager=BmobUserManager.getInstance(this);

        if(userName!=null&&password!=null){
            edt_username.setText(userName);
            edt_pwd.setText(password);
        }
    }

    @Event(value = R.id.btn_login,type = View.OnClickListener.class)
    private  void  doLogin(View  v){
        LoadingDialog.showLoadingDialog(LoginActivity.this);
        userName=edt_username.getText().toString().trim();
        password=edt_pwd.getText().toString().trim();
        if(TextUtils.isEmpty(userName)||
                TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "用户名和密码不能为空...", Toast.LENGTH_SHORT).show();
            return;
        }
        User  user=new User();
        user.setUsername(userName);
        user.setPassword(password);
        user.setEmail(userName);
        user.setMobilePhoneNumber(userName);

        user.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                LoadingDialog.dissmissDialog();
                //给当前用户赋值
                MyApplication.mCurrentUser = getCurrentUser(LoginActivity.this, User.class);
                Toast.makeText(LoginActivity.this, "登录成功...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                updateUserInfos();
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                LoadingDialog.dissmissDialog();
                Toast.makeText(LoginActivity.this,msg, Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(LoginActivity.this, "有待进一步开发...", Toast.LENGTH_SHORT).show();
//                LoadingDialog.showLoadingDialog(LoginActivity.this);
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.tv_see_first:
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                break;
        }
    }



    /** 用于登陆或者自动登陆情况下的用户资料及好友资料的检测更新
     * @Title: updateUserInfos
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    public void updateUserInfos(){
        //更新地理位置信息
        updateUserLocation();
        //查询该用户的好友列表(这个好友列表是去除黑名单用户的哦),目前支持的查询好友个数为100，如需修改请在调用这个方法前设置BmobConfig.LIMIT_CONTACTS即可。
        //这里默认采取的是登陆成功之后即将好于列表存储到数据库中，并更新到当前内存中,
        userManager.queryCurrentContactList(new FindListener<BmobChatUser>() {

            @Override
            public void onError(int arg0, String arg1) {
                // TODO Auto-generated method stub
                if(arg0== BmobConfig.CODE_COMMON_NONE){
                    LogUtil.i(arg1);
                }else{
                    LogUtil.i("查询好友列表失败：" + arg1);
                }
            }

            @Override
            public void onSuccess(List<BmobChatUser> arg0) {
                // TODO Auto-generated method stub
                // 保存到application中方便比较
                MyApplication.mInstance.setContactList(CollectionUtils.list2map(arg0));
            }
        });
    }

    /** 更新用户的经纬度信息
     * @Title: uploadLocation
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    public void updateUserLocation(){
        if( MyApplication.mInstance.lastPoint!=null){
            String saveLatitude  = MyApplication.mInstance.getLatitude();
            String saveLongtitude =MyApplication.mInstance.getLongtitude();
            String newLat = String.valueOf(MyApplication.mInstance.lastPoint.getLatitude());
            String newLong = String.valueOf(MyApplication.mInstance.lastPoint.getLongitude());
//			ShowLog("saveLatitude ="+saveLatitude+",saveLongtitude = "+saveLongtitude);
//			ShowLog("newLat ="+newLat+",newLong = "+newLong);
            if(!saveLatitude.equals(newLat)|| !saveLongtitude.equals(newLong)){//只有位置有变化就更新当前位置，达到实时更新的目的
                User u = (User) userManager.getCurrentUser(User.class);
                final User user = new User();
                user.setLocation(MyApplication.mInstance.lastPoint);
                user.setObjectId(u.getObjectId());
                user.update(this,new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        MyApplication.mInstance.setLatitude(String.valueOf(user.getLocation().getLatitude()));
                        MyApplication.mInstance.setLongtitude(String.valueOf(user.getLocation().getLongitude()));
                        LogUtil.i("经纬度更新成功");
                    }
                    @Override
                    public void onFailure(int code, String msg) {
                        // TODO Auto-generated method stub
                        LogUtil.i("经纬度更新 失败:" + msg);
                    }
                });
            }else{
                LogUtil.i("用户位置未发生过变化");
            }
        }
    }




}
