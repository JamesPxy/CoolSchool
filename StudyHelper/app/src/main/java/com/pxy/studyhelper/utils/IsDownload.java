package com.pxy.studyhelper.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class IsDownload {
	@SuppressLint({ "NewApi", "CommitPrefEdits" })
	public static void saveDownloadStatus(Context context, String position,boolean isDown){
		//从偏好设置中读取当前试卷是否已经下载
		SharedPreferences pref=context.getSharedPreferences("download", Context.MODE_WORLD_WRITEABLE);
		Editor editor=pref.edit();
		editor.putBoolean(position,isDown);
		editor.commit();
	}

	@SuppressLint("NewApi")
	public static boolean isDownload(Context context, String position) {
		//从偏好设置中读取当前位置是否已经下载
		SharedPreferences pref=context.getSharedPreferences("download",Context.MODE_WORLD_WRITEABLE);
		if(pref.getBoolean(position, false)){
			return  true;
		}
		return false;
	}
}