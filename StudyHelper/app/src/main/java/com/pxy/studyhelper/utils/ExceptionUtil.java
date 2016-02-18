package com.pxy.studyhelper.utils;

import android.util.Log;
import com.pxy.studyhelper.MyApplication;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {

	public static void handleException(Exception e){
		if(MyApplication.isDebug){
			e.printStackTrace();
		}else{//发布出去之后，将用户使用的异常信息发送给开发者
			StringWriter  sw=new StringWriter();
			PrintWriter printWriter=new PrintWriter(sw);
			e.printStackTrace(printWriter);
			String errorInfo = sw.toString();
			// 联网发送给开发人员
			Log.i("ExpUtil error info--", errorInfo);
		}
	}

}
