package com.pxy.studyhelper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.pxy.studyhelper.R;
import com.pxy.studyhelper.entity.Topic;
import com.pxy.studyhelper.utils.DialogUtil;
import com.pxy.studyhelper.utils.Tools;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

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
    private  String picturePath;
    private final Topic  topic=new Topic();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }


    @Event(value = {R.id.iv_send,R.id.iv_add_img},type = View.OnClickListener.class)
    private void doClick(View  view){
        switch (view.getId()){
            case R.id.iv_send:
                if(picturePath!=null)uploadImg(MakeTopicActivity.this, picturePath, topic);
                else Tools.ToastShort("get img path  null");
                break;

            case R.id.iv_add_img:
                selectImgPic();
                break;
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
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            mImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            LogUtil.e("img  path--"+picturePath);
        }
    }

    private void uploadImg(final Context context,String picPath, final Topic  topic){
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(context, new UploadFileListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                //bmobFile.getUrl()---返回的上传文件的地址（不带域名）
                //bmobFile.getFileUrl(context)--返回的上传文件的完整地址（带域名）
                LogUtil.i("上传文件成功" + bmobFile.getFileUrl(context));
                topic.setImage(bmobFile);
                topic.setLove(0);
                topic.setUserName("test cool  666");
//                topic.setContent("zly  lbj  pxy  zai yi qi");
                topic.setContent(mEdtContent.getText().toString());
//                BmobUser bmobUser = BmobUser.getCurrentUser(context);
//                if(bmobUser != null){
//                    // 允许用户使用应用
//                }else{
//                    //缓存用户对象为空时， 可打开用户注册界面…
//                }
//                topic.setUserName(MyApplication.mCurrentUser.getUsername());
//                topic.setUserName(User.getCurrentUser(context).getUsername());
                topic.save(context, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        LogUtil.i("save  topic  success");
                        DialogUtil.closeProgressDialog();
                        Tools.ToastShort("动态发表成功...");
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
                DialogUtil.showProgressDialog(context, "uploading..." + value + "%");
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
}
