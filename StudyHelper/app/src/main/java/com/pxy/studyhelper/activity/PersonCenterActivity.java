package com.pxy.studyhelper.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pxy.studyhelper.MyApplication;
import com.pxy.studyhelper.R;
import com.pxy.studyhelper.entity.User;
import com.pxy.studyhelper.utils.CompressImage;
import com.pxy.studyhelper.utils.DialogUtil;
import com.pxy.studyhelper.utils.Tools;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


@ContentView(value = R.layout.activity_person_center)
public class PersonCenterActivity extends AppCompatActivity {

    @ViewInject(value = R.id.iv_user_head)
    private ImageView  ivHead;
    @ViewInject(value = R.id.tv_user_name)
    private TextView tvName;
    @ViewInject(value = R.id.tv_sex)
    private TextView tvSex;
    @ViewInject(value = R.id.tv_school)
    private TextView tvSchool;
    @ViewInject(value = R.id.tv_sign)
    private TextView  tvSign;


    private int RESULT_LOAD_IMG=100;
    boolean  isCompressSuccess=false;
    private User  mUser;
    private String  newPath;
    private boolean  isUpdate=false;//标记用户是否有更改信息
    private boolean  isMale;//标记性别  男 true 女  false
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_person_center);
        x.view().inject(this);
        setTitle();
        if (MyApplication.mCurrentUser != null) {
            mUser = MyApplication.mCurrentUser;
        } else {
            //登录
            Tools.ToastShort("请先登录");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
        setView();

    }

    private void setView() {
        x.image().bind(ivHead, mUser.getHeadUrl(), MyApplication.imageOptions);
        tvName.setText(mUser.getUsername());
        if(mUser.getSex()!=null){
            isMale=mUser.getSex();
            tvSex.setText(isMale ? "男" : "女");
        }
        tvSchool.setText(mUser.getSchool());
        if(mUser.getSign()!=null)tvSign.setText(mUser.getSign());
    }

    private void setTitle() {
        RelativeLayout rv= (RelativeLayout) findViewById(R.id.include_tabbar);
        ImageView iv_back= (ImageView) rv.findViewById(R.id.iv_back);
        TextView title=(TextView)rv.findViewById(R.id.tv_title);
        TextView tvSave=(TextView)rv.findViewById(R.id.tv_save);
        tvSave.setVisibility(View.VISIBLE);
        title.setText("个人中心");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showAlert();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        

    }

    //  2016-02-23 ////  提醒用户保存信息,如果有更改信息
    @Override
    public void onBackPressed() {
        showAlert();
//        super.onBackPressed();
    }

    /**
     * 提醒用户修改信息
     */
    private void showAlert() {
        if(isUpdate){
            new AlertDialog.Builder(PersonCenterActivity.this).setIcon(R.drawable.ic_luncher)
                    .setTitle("提醒")
                    .setMessage("是否保存并提交修改信息")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateUserInfo();
                            finish();
                        }
                    }).show();
        }else{
            finish();
        }
    }

    @Event(value = {R.id.rv_mine,R.id.rv_name,R.id.rv_sex,R.id.rv_school,R.id.tv_save,R.id.rv_sign},type = View.OnClickListener.class)
    private void doClick(View view){
        switch (view.getId()){
            case R.id.tv_save:
                updateUserInfo();
                isUpdate=false;
                break;
            case R.id.rv_mine:
                //2016-02-23    更换头像
                selectImgPic();
                break;
            case R.id.rv_name:updateUser(1);break;
            case R.id.rv_sex:updateSex();break;
            case R.id.rv_school:updateUser(2);break;
            case R.id.rv_sign:updateUser(3);break;
        }
    }

    private void updateSex() {
        AlertDialog.Builder  builder=new AlertDialog.Builder(PersonCenterActivity.this);
        String[]  data={"男","女"};
        builder.setSingleChoiceItems(data,0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isUpdate=true;
                switch (which){
                    case 0:isMale=true;tvSex.setText("男");mUser.setSex(true);dialog.dismiss();break;
                    case 1:isMale=false;tvSex.setText("女");mUser.setSex(false);dialog.dismiss();break;
                }
            }
        });
        builder.show();
    }

    private void updateUser(final int type) {
        AlertDialog.Builder  builder=new AlertDialog.Builder(PersonCenterActivity.this);
        View view=View.inflate(PersonCenterActivity.this, R.layout.update_info_layout, null);
        final EditText  edt= (EditText) view.findViewById(R.id.editText);
        builder.setTitle("  ");
        builder.setView(view);
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String  s=edt.getText().toString().trim();
                if(!TextUtils.isEmpty(s)){
                    isUpdate=true;
                    switch (type){
                        case 1:tvName.setText(s);mUser.setUsername(s);break;
                        case 2:tvSchool.setText(s);mUser.setSchool(s);break;
                        case 3:tvSign.setText(s);mUser.setSign(s);break;
                    }
                }
            }
        });
        builder.show();
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
            ivHead.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            //todo  压缩文件
            if(picturePath!=null)compressImageFile(picturePath);

            uploadHead(PersonCenterActivity.this);
            LogUtil.e("img  path--" + picturePath);
        }
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
        isCompressSuccess= CompressImage.compressFromBpToFile(PersonCenterActivity.this, image);
        if(isCompressSuccess)newPath=this.getCacheDir().getAbsolutePath()+"/topicImages.jpg";
    }

    private void uploadHead(final Context context) {
        if (newPath != null) {
            {//有上传头像时 newPath不为空 上传头像
                DialogUtil.showProgressDialog(context,"uploading...");
                final BmobFile file = new BmobFile(new File(newPath));
                file.uploadblock(context, new UploadFileListener() {
                    @Override
                    public void onSuccess() {
                        LogUtil.i("上传头像成功");
                        mUser.setHeadUrl(file.getFileUrl(context));
                        updateUserInfo();
                    }

                    @Override
                    public void onProgress(Integer value) {
                        super.onProgress(value);
                        if(value==100)DialogUtil.closeProgressDialog();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        LogUtil.e("上传头像失败--"+s);
                        DialogUtil.closeProgressDialog();
                    }
                });
            }
        }

    }

    private void updateUserInfo(){
//        mUser.setSex(true);
//        mUser.setScore(1000);
//        mUser.setSchool("清华大学");
        mUser.setLevel(6);
//        mUser.setSign("我就是我,汹涌澎湃!");
//            , MyApplication.mCurrentUser.getObjectId()
        mUser.update(PersonCenterActivity.this,new UpdateListener() {
            @Override
            public void onSuccess() {
                LogUtil.i(mUser.toString());
                Tools.ToastShort("更新信息成功...");
            }
            @Override
            public void onFailure(int i, String s) {
                Tools.ToastShort("更新信息失败..." + s);
            }
        });
    }

}
