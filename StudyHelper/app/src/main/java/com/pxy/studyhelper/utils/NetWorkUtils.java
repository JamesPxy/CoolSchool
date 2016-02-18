package com.pxy.studyhelper.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.pxy.studyhelper.R;


/**
 * 检测用户手机网络状态
 * @author pxy
 */
public class NetWorkUtils {

	public static void CheckNetworkState(final Activity activity){
		ConnectivityManager  cm=(ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo=cm.getActiveNetworkInfo();
		Tools.ShowLog("networkinfo--", networkInfo);
		if(networkInfo==null){
			new AlertDialog.Builder(activity)
					.setTitle("提醒")
					.setMessage("亲，请打开网络")
					.setIcon(R.drawable.ic_luncher)
					.setPositiveButton("确定", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent i=new Intent(Settings.ACTION_WIRELESS_SETTINGS);
							activity.startActivity(i);
						}
					})
					.setNegativeButton("取消", null)
					.show();
		}
//		else{
//			int type=networkInfo.getType();
//			if(type==ConnectivityManager.TYPE_MOBILE)
//			{
////				TApplication.currentNetWorkState=Const.NETWORK_TYPE_MOBILE;
//			}else if(type==ConnectivityManager.TYPE_WIFI){
////				TApplication.currentNetWorkState=Const.NETWORK_TYPE_WIFI;
//			}
//		}
	}
}
