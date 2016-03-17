package com.pxy.studyhelper.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.pxy.studyhelper.MyApplication;

import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * User: Pxy(15602269883@163.com)
 * Date: 2016-02-04
 * Time: 22:04
 * FIXME
 */
public class Tools {


    //吐司工具类  短时间吐司
    public static void ShowToast(Context context,String msg)
    {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    public static void ToastShort(String msg)
    {
        Toast.makeText(x.app(), msg, Toast.LENGTH_SHORT).show();
    }

    //log日志工具类
    public static  void ShowLog(String tag,Object  msg){
        if(MyApplication.isDebug){
            Log.i(tag, String.valueOf(msg));
        }
    }

    /**
     * 得当前的版本号 manifest.xml中的versionName
     * @param context
     * @return
     * @throws Exception
     */
    public static  String  getCurrentVersionName(Context  context){
        PackageManager manager=context.getPackageManager();
        String packageName=context.getPackageName();
        PackageInfo packageInfo= null;
        try {
            packageInfo = manager.getPackageInfo(packageName,0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 检查是否有网络 */
    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }

    /** 检查是否是WIFI */
    public static boolean isWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI)
                return true;
        }
        return false;
    }

    /** 检查是否是移动网络 */
    public static boolean isMobile(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
        }
        return false;
    }

    private static NetworkInfo getNetworkInfo(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /** 检查SD卡是否存在 */
    public static boolean checkSdCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }



    /**
     * 从sdcard读取数据
     * @param path
     * @return
     */
    public static byte[]  readFileFromSdcard(String path){
        byte [] data=null;
        FileInputStream in=null;
        try {
            in=new FileInputStream(path);
            int size=in.available();
            data=new byte[size];
            in.read(data);
        } catch (IOException e) {
            ExceptionUtil.handleException(e);
        }finally{
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    /**
     * 写文件到sdcard
     * @param path
     * @param filename
     * @param data
     * @throws Exception
     */
    public static void writeFileToSdcard(String path,String filename,byte[] data) throws Exception{

        FileOutputStream  out=null;
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            //sdcard存在 新建文件
            File fileDir=new File(path);
            if(fileDir.exists()){
                fileDir.mkdirs();
            }
            File file=new File(fileDir, filename);
            if(file.exists()){
                file.delete();
            }
            try {
                out=new FileOutputStream(file);
                out.write(data);
                out.flush();
            } catch (Exception e) {
                ExceptionUtil.handleException(e);
            }finally{
                out.close();
            }
        }
    }


    /**
     * 返回Activity是否在栈顶
     *
     * @param context
     * @param activityName
     * @return true - 在栈顶
     */
    public static boolean isTopActivity(Context context, String activityName) {
        boolean isTop = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if (cn.getClassName().contains(activityName)) {
            isTop = true;
        }
        return isTop;
    }

    /**
     * 显示或隐藏软键盘
     */
    public static void showOrHideSoftInput(boolean isShow, View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            view.requestFocus();
            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        } else {
            if (isSoftKeyBoardShowing(view)) {
                view.clearFocus();
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (activity.getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 判断软键盘是否正在显示
     */
    public static boolean isSoftKeyBoardShowing(View edit) {
        boolean bool = false;
        InputMethodManager imm = (InputMethodManager) edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            bool = true;
        }
        return bool;
    }

    /**
     * 检测该包名所对应的应用是否存在
     *
     * @param packageName
     * @return
     */

    public static boolean existPackage(String packageName, Context context) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

    }

    /**
     * 删除所有Cookie
     *
     * @param context
     */
    public static void clearCookie(Context context) {
        CookieSyncManager cookieSyncMngr =
                CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
//        String CookieStr = cookieManager.getCookie("http://rcp.dev.jxzy.com/courseContent.html");
//        Log.e("Cookie", CookieStr);
        cookieManager.removeAllCookie();
    }

}  
