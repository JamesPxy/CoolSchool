package com.pxy.studyhelper.adapter;

import android.content.Context;

import com.pxy.studyhelper.entity.Topic;
import com.pxy.studyhelper.utils.DialogUtil;
import com.pxy.studyhelper.utils.Tools;

import org.xutils.common.util.LogUtil;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * User: Pxy(15602269883@163.com)
 * Date: 2016-02-17
 * Time: 21:21
 * FIXME
 */
public class UploadTopicBiz {
    private Context context;
    public UploadTopicBiz(Context context){
        this.context=context;
    }

    public   Topic  uploadTopicImage(final Topic  topic,String picPath){

        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(context, new UploadFileListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                //bmobFile.getUrl()---返回的上传文件的地址（不带域名）
                //bmobFile.getFileUrl(context)--返回的上传文件的完整地址（带域名）
                Tools.ToastShort("发表动态成功"+bmobFile.getFileUrl(context));
                topic.setImage(bmobFile);
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
                DialogUtil.showProgressDialog(context,"已上传"+value);
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                Tools.ToastShort(code+"上传文件失败---"+msg);
            }
        });
        return topic;
    }

    public  void uploadTopic(Topic  topic){
        if(topic.getImage()==null){
            Tools.ToastShort("上传照片失败,请重试...");
            return;
        }
        topic.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                LogUtil.i("upload  topic success  666");
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtil.e(i+"upload  topic error"+s);
            }
        });
    }
}
