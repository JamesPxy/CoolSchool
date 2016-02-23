package com.pxy.studyhelper.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pxy.studyhelper.R;

public class DialogUtil {
	private static ProgressDialog  progressDialog=null;
	public  static void showProgressDialog(Context context,String message){
		if(progressDialog==null){
			progressDialog=new ProgressDialog(context);
			progressDialog.setMessage(message);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}
	}

	public static void closeProgressDialog(){
		if(progressDialog!=null){
			progressDialog.cancel();
			progressDialog=null;
			//提醒垃圾回收机制回收
			System.gc();
		}
	}

	public static void showResultDialog(Context context, int score){
		AlertDialog.Builder  builder=new AlertDialog.Builder(context);
		View view= LayoutInflater.from(context).inflate(R.layout.result_dialog_layout, null);
		ImageView  imageView= (ImageView) view.findViewById(R.id.imageView);
		TextView   textView= (TextView) view.findViewById(R.id.textView);
		String string;
		if(score<60){
			string="不要气馁,加倍努力吧!";
			imageView.setImageResource(R.drawable.degree0);
		}else  if(60<=score&&score<90){
			string="做的不错,继续加油!";
			imageView.setImageResource(R.drawable.degree1);
		}else if(90<=score&&score<=99){
			string="干的漂亮,keep fighting!";
			imageView.setImageResource(R.drawable.degree2);
		}else {
			string="简直完美,不许骄傲喔!";
			imageView.setImageResource(R.drawable.degree3);
		}
		textView.setText("您本次测试成绩为:"+score+"分"+"\n"+string);
		builder.setTitle("考试成绩")
				.setView(view)
				.setIcon(R.drawable.ic_luncher);
//        builder.setNegativeButton("取消",null);
		builder.show();
	}

	public static void  showDialog(Context  context,String  msg){
		AlertDialog.Builder  builder=new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_luncher)
				.setTitle("提示")
				.setMessage(msg)
				.setNegativeButton("取消",null)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		builder.show();
	}




}
