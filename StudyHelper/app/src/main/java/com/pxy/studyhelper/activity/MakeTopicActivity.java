package com.pxy.studyhelper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.pxy.studyhelper.MyApplication;
import com.pxy.studyhelper.R;
import com.pxy.studyhelper.entity.Topic;
import com.pxy.studyhelper.fragment.TopicFragment;
import com.pxy.studyhelper.utils.CompressImage;
import com.pxy.studyhelper.utils.DialogUtil;
import com.pxy.studyhelper.utils.Tools;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * User: Pxy(15602269883@163.com)
 * Date: 2016-02-17
 * Time: 21:12
 * FIXME
 */
@ContentView(value = R.layout.activity_make_topic)
public class MakeTopicActivity   extends Activity {

    @ViewInject(value = R.id.editText)
    private EditText  mEdtContent;
    @ViewInject(value = R.id.iv_add_img)
    private ImageView  mImageView;

    private static final int RESULT_LOAD_IMG=1;
    private  boolean isCompressSuccess=false;
    private String  newPath;
    private final Topic  topic=new Topic();
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        isCompressSuccess=getIntent().getBooleanExtra("boolean",false);
        if(isCompressSuccess){
            newPath=this.getCacheDir().getAbsolutePath()+"/topicImages.jpg";
            mImageView.setImageBitmap(BitmapFactory.decodeFile(newPath));
        }
    }


    @Event(value = {R.id.iv_send,R.id.iv_add_img,R.id.iv_back},type = View.OnClickListener.class)
    private void doClick(View  view){
        switch (view.getId()){
            case R.id.iv_send:
                uploadImg(MakeTopicActivity.this, newPath, topic);
                break;
            case R.id.iv_add_img:
                selectImgPic();
                break;
            case R.id.iv_back:finish();break;
        }
    }


    private void selectImgPic() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG&& resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String  picturePath = cursor.getString(columnIndex);
            cursor.close();
            mImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            //todo  压缩文件
            if(picturePath!=null)compressImageFile(picturePath);
            LogUtil.e("img  path--"+picturePath);
        }
    }

    private void uploadImg(final Context context,String picPath, final Topic  topic){
        final String  content=mEdtContent.getText().toString().trim();
        if(TextUtils.isEmpty(content)){
            Tools.ToastShort("动态内容不能为空...");
            return;
        }
        if(picPath==null){
            //用户未上传照片
            topic.setLove(0);
            topic.setContent(content);
            if(MyApplication.mCurrentUser!=null){
                topic.setUserName(MyApplication.mCurrentUser.getUsername());
                topic.setHeadUrl(MyApplication.mCurrentUser.getHeadUrl());
                topic.setUserId(MyApplication.mCurrentUser.getObjectId());
            }else{
                topic.setUserName("null");
            }
            topic.save(context, new SaveListener() {
                @Override
                public void onSuccess() {
                    LogUtil.i("动态发表成功 success");
                    Tools.ToastShort("动态发表成功...");
                    finish();
                }
                @Override
                public void onFailure(int i, String s) {
                    LogUtil.e(i + "动态发表失败" + s);
                    Tools.ToastShort("动态发表失败..."+s);
                }
            });
            return;
        }
        if(!isCompressSuccess){
            Tools.ToastShort("压缩图片失败,请重试...");
            return;
        }
        //压缩图片
        DialogUtil.showProgressDialog(context, "uploading...");
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(context, new UploadFileListener() {
            @Override
            public void onSuccess() {
                LogUtil.i("上传文件成功" + bmobFile.getFileUrl(context));
                topic.setImage(bmobFile);
                topic.setLove(0);
                topic.setContent(content);
                if (MyApplication.mCurrentUser != null) {
                    topic.setUserName(MyApplication.mCurrentUser.getUsername());
                    topic.setHeadUrl(MyApplication.mCurrentUser.getHeadUrl());
                    topic.setUserId(MyApplication.mCurrentUser.getObjectId());
                }
                topic.save(context, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        LogUtil.i("save  topic  success");
                        DialogUtil.closeProgressDialog();
                        Tools.ToastShort("动态发表成功...");
                        TopicFragment.mTopicList.addFirst(topic);
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        LogUtil.e(i + "save  topic  error" + s);
                        DialogUtil.closeProgressDialog();
                    }
                });
            }

            @Override
            public void onProgress(Integer value) {
                // TODO Auto-generated method stub
                // 返回的上传进度（百分比）
                if(value==100){
                    DialogUtil.closeProgressDialog();
                }
//                DialogUtil.showProgressDialog(context, "uploading..." + value + "%");
                LogUtil.i("on progress--" + value);
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                Tools.ToastShort("上传文件失败：" + msg);
                DialogUtil.closeProgressDialog();
            }
        });

    }

    private void  compressImageFile(String  path){
        //先将所选图片转化为流的形式，path所得到的图片路径
        FileInputStream is = null;
        try {
            is = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int size = 4;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = size;
        //将图片缩小为原来的  1/size ,不然图片很大时会报内存溢出错误
        Bitmap image = BitmapFactory.decodeStream(is,null,options);
        //显示在本地
//        mImageView.setImageBitmap(image);
        try {
            if(is!=null) is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        compressImage(image);
        newPath=this.getCacheDir().getAbsolutePath()+"/topicImages.jpg";
        isCompressSuccess= CompressImage.compressFromBpToFile(MakeTopicActivity.this,image);
    }

    private void compressImage(Bitmap image) {
        //定义一个file，为压缩后的图片   File f = new File("图片保存路径","图片名称");
        newPath=MakeTopicActivity.this.getCacheDir().getAbsolutePath()+"/topicImages.jpg";
        File file=new File(newPath);
        if(file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            LogUtil.e(e.getMessage());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//这里100表示不压缩，将不压缩的数据存放到baos中
        int per = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, per, baos);// 将图片压缩为原来的(100-per)%，把压缩后的数据存放到baos中
            per -= 10;// 每次都减少10
        }
        //回收图片，清理内存
        if(image != null && !image.isRecycled()){
            image.recycle();
            image = null;
            System.gc();
        }
        //将输出流写入到新文件
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            baos.close();
            //标记压缩图片成功
            isCompressSuccess=true;
        }catch (Exception e){
            LogUtil.e(e.getMessage());
        }
    }
}
