package com.pxy.studyhelper.biz;

import android.content.Context;
import android.content.Intent;

import com.pxy.studyhelper.activity.TestListActivity;
import com.pxy.studyhelper.entity.Test;
import com.pxy.studyhelper.utils.Constant;
import com.pxy.studyhelper.utils.IsDownload;
import com.pxy.studyhelper.utils.LoadingDialog;
import com.pxy.studyhelper.utils.Tools;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
/**
 * User: Pxy(15602269883@163.com)
 * Date: 2016-02-04
 * Time: 22:43
 * FIXME
 */
public class GetExamDataBiz {
    /**
     * 获取下载文件对象 name url
     * @param context
     * @param fileName
     * //        NETWORK_ELSE_CACHE:先从网络读取数据，如果没有，再从缓存中获取。
    //        CACHE_ELSE_NETWORK:先从缓存读取数据，如果没有，再从网络获取。
    //        CACHE_THEN_NETWORK:先从缓存取数据，无论结果如何都会再次从网络获取数据。也就是说会产生2次调用。
     */
    public static void getExamData(final Context context,int sort1,int sort2){
        BmobQuery<Test>  bmobQuery=new BmobQuery<>();
        bmobQuery.addWhereEqualTo("sorts1", sort1);
        bmobQuery.addWhereEqualTo("sorts2",sort2);

        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 先从缓存获取数据，如果没有，再从网络获取。
//        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);//先从网络读取数据，如果没有，再从缓存中获取。
        // 返回50条数据，如果不加上这条语句，默认返回10条数据
        bmobQuery.setLimit(50);
        bmobQuery.findObjects(context, new FindListener<Test>() {
            @Override
            public void onSuccess(List<Test> object) {
                LogUtil.e("get exam data success--size--"+object.size());
                TestListActivity testListActivity = (TestListActivity) context;
                testListActivity.setListView(object);
            }
            @Override
            public void onError(int code, String msg) {
                Tools.ShowToast(context, "get data error--" + msg);
                LoadingDialog.dissmissDialog();
            }
        });
    }
    /**
     * 下载对应url的文件
     * @param context
     * @param uri
     * @param dbName
     * @throws IOException
     */
    public static void download(Context context,Test test) throws IOException {
//        File DB_PATH = context.getFilesDir();
//        String DB_NAME = dbName;
        String uri=test.getTestFile().getFileUrl(context);
        String dbName=test.getTestFile().getFilename();
        File  file=new File(context.getFilesDir(),dbName);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        InputStream is=null;
        OutputStream os=null;

        try {
            URL url=new URL(uri);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setUseCaches(true);
            is=conn.getInputStream();
//            os = new FileOutputStream(DB_PATH + DB_NAME);
            os = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while((length = is.read(buffer)) > 0)
            {
                os.write(buffer, 0, length);
                LogUtil.i("download  ing...");
                os.flush();
            }
            //保存对应url下载成功
            IsDownload.saveDownloadStatus(context, uri, true);
            LogUtil.i("download  test  success  666");
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
            IsDownload.saveDownloadStatus(context, uri,false);
        }finally {
            if (os != null) {
                os.close();
            }
            is.close();
            Intent  intent=new Intent();
            intent.setAction(Constant.RECEIVER_DOWNLOAD);
            intent.putExtra("uri",uri);
            intent.putExtra("test",test);
            context.sendBroadcast(intent);
        }
    }


    /**
     * 测试下载类
     * @param context
     * @param url
     * @param dbName
     */
    public static void test(final Context  context, final String url, final String  dbName){
        RequestParams params = new RequestParams(url);
        File  file=new File(context.getFilesDir(),dbName);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            return;
        }

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                // 成功下载，
                LogUtil.i("onSuccess------" + result);
                OutputStream  outputStream= null;
                try {
                    outputStream = context.openFileOutput(dbName, Context.MODE_WORLD_WRITEABLE);

                    outputStream.write(result.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //保存对应url下载成功
                IsDownload.saveDownloadStatus(context,url,true);
            }
            @Override
            public void onCancelled(CancelledException arg0) {

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                LogUtil.e("error---"+arg0.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("error---onFinished");
            }

        });
    }


}  
