package com.pxy.studyhelper.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
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
    public static  String  getCurrentVersion(Context  context) throws PackageManager.NameNotFoundException {
        PackageManager manager=context.getPackageManager();
        String packageName=context.getPackageName();
        PackageInfo packageInfo=manager.getPackageInfo(packageName, 0);
        return packageInfo.versionName;
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

}  
