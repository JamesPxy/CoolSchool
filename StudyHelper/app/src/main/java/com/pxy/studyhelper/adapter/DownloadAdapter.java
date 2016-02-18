package com.pxy.studyhelper.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pxy.studyhelper.R;
import com.pxy.studyhelper.activity.ExamActivity;
import com.pxy.studyhelper.activity.PracticeActivity;
import com.pxy.studyhelper.biz.GetExamDataBiz;
import com.pxy.studyhelper.entity.Test;
import com.pxy.studyhelper.utils.DialogUtil;
import com.pxy.studyhelper.utils.IsDownload;
import com.pxy.studyhelper.utils.Tools;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Pxy(15602269883@163.com)
 * Date: 2016-02-05
 * Time: 17:20
 * FIXME
 */
public class DownloadAdapter  extends BaseAdapter {

    private List<Test>  mList;
    private Context  context;
    public DownloadAdapter(Context context,List<Test> list){
        this.context=context;
        if(list!=null){
            mList=list;
        }else{
            mList=new ArrayList<>();
        }

    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder  holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_download,null);
            holder.mRelativeLayout= (RelativeLayout) convertView.findViewById(R.id.relativelayout);
            holder.mLinearLayout= (LinearLayout) convertView.findViewById(R.id.lv_download);
            holder.mImageView= (ImageView) convertView.findViewById(R.id.iv_download);
            holder.tvTestName= (TextView) convertView.findViewById(R.id.tv_testName);
            holder.tvDownload= (TextView) convertView.findViewById(R.id.tv_download);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        final Test data=mList.get(position);
        holder.tvTestName.setText(data.getName());
        if(IsDownload.isDownload(context,data.getTestFile().getFileUrl(context))) {
            holder.mImageView.setImageResource(R.drawable.download_click_icon);
            holder.tvDownload.setText("已下载");
            holder.tvDownload.setTextColor(Color.BLUE);
        }
        //点击item事件
        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 2016/2/5  弹出对话框选择进入测试模式 或者练习模式 以及错题模式
                AlertDialog.Builder  builder=new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.ic_luncher);
                builder.setTitle("选择模式:");
                builder.setItems(new String[]{"练习模式", "测试模式", "错题模式"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:case 2:{
                                Intent intent = new Intent(context, PracticeActivity.class);
                                //试题类别
                                intent.putExtra("dbName", data.getTestFile().getFilename());
                                //做题模式
                                intent.putExtra("mode",which);
                                context.startActivity(intent);
                            }
                            break;
                            case 1:{
                                Intent intent = new Intent(context, ExamActivity.class);
                                //试题类别
                                intent.putExtra("dbName", data.getTestFile().getFilename());
                                context.startActivity(intent);
                            }

                        }
                    }
                });
                builder.show();

            }
        });
        //下载按钮
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo  下载判断和下载
                LogUtil.i(data.getTestFile().getFileUrl(context));
                if(IsDownload.isDownload(context,data.getTestFile().getFileUrl(context))) {
                    Tools.ToastShort("该试题已下载...");
                }else{//没有下载  进行下载
                    DialogUtil.showProgressDialog(context,"正在下载中...");
                    new  Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                GetExamDataBiz.download(context, data);
                                LogUtil.e(data.getTestFile().getFilename());
                            } catch (Exception e) {
                                LogUtil.e(e.getMessage());
                                return;
                            }
                        }
                    }).start();
                }
            }
        });

        return convertView;
    }

    class ViewHolder{
        TextView tvTestName;
        TextView  tvDownload;
        ImageView mImageView;
        LinearLayout mLinearLayout;
        RelativeLayout  mRelativeLayout;
    }
}
