package com.pxy.studyhelper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.pxy.studyhelper.utils.Tools;

import cn.bmob.im.BmobUserManager;

/** 除登陆注册和欢迎页面外继承的基类-用于检测是否有其他设备登录了同一账号
 * @ClassName: ActivityBase
 * @Description: TODO
 * @author pxy
 * @date 2016-2-26
 */
public class ActivityBase extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//自动登陆状态下检测是否在其他设备登陆
		checkLogin();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//锁屏状态下的检测
		checkLogin();
	}

	public void checkLogin() {
		BmobUserManager userManager = BmobUserManager.getInstance(this);
		if (userManager.getCurrentUser() == null) {
			Tools.ToastShort("您的账号已在其他设备上登录!");
			startActivity(new Intent(this, LoginActivity.class));
			finish();
		}
	}

	/** 隐藏软键盘
	 * hideSoftInputView
	 * @Title: hideSoftInputView
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	public void hideSoftInputView() {
		InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

}
