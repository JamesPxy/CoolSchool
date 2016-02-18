package com.pxy.studyhelper.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import com.pxy.studyhelper.R;

/**
 * Created by ljy on 2016-01-29.
 */
public class LoadingDialog {

    private static AlertDialog  mAlertDialog;

    public static void showLoadingDialog(Context context){
        AlertDialog.Builder  builder=new AlertDialog.Builder(context);
        ImageView  mImageView=new ImageView(context);
//        builder.setTitle("login")
//                .setView(view)
//                .setCancelable(true)
//                .setIcon(R.drawable.ic_luncher)
//                .setMessage("拼命加载中...");
//        builder.setNegativeButton(null);
        builder.setView(mImageView);

        //加载动画
        mImageView.setImageResource(R.drawable.annimation_loading);
        AnimationDrawable  animationDrawable = (AnimationDrawable) mImageView.getDrawable();
        animationDrawable.start();

        mAlertDialog=builder.create();
        mAlertDialog.show();
    }
    public    static  void dissmissDialog(){
        if(mAlertDialog!=null){
            mAlertDialog.dismiss();
        }
        mAlertDialog=null;
    }



}



//AlertDialog.Builder  builder=new AlertDialog().Builder(context);
//View view=LayoutInflater.from(context).inflate(R.layout.activity_login, null);
//builder.setTitle("login")
//        .setView(view)
//        .setCancelable(true)
//        .setIcon(R.drawable.ic_luncher)
//        .setMessage("msg");
//        builder.setNegativeButton("取消",null);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//@Override
//public void onClick(DialogInterface dialog, int which) {
//
//        }
//        });
//        AlertDialog  mAlertDialog   mAlertDialog=builder.create();
//        mAlertDialog.show();