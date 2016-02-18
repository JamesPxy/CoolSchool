package com.pxy.studyhelper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pxy.studyhelper.R;
import com.pxy.studyhelper.entity.Topic;
import com.pxy.studyhelper.utils.Tools;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Pxy(15602269883@163.com)
 * Date: 2016-02-16
 * Time: 15:30
 * FIXME
 */
public class TopicAdapter  extends BaseAdapter {
    private Context  context;
    private List<Topic>  mTopicList;
    private ImageOptions imageOptions;

    public TopicAdapter(Context  context,List<Topic>  mTopicList){
        this.context=context;
        if(mTopicList!=null){
            this.mTopicList=mTopicList;
        }else{
            this.mTopicList=new ArrayList<>();
        }

         imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))//图片大小
                .setRadius(DensityUtil.dip2px(5))//ImageView圆角半径
                .setCrop(true)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.user_head)//加载中默认显示图片
                .setFailureDrawableId(R.drawable.user_head)//加载失败后默认显示图片
                .build();

    }
    @Override
    public int getCount() {
        return mTopicList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTopicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder  holder;
        if(view==null){
            holder=new ViewHolder();
            view= LayoutInflater.from(context).inflate(R.layout.item_topic,null);
            holder.ivUserHead= (ImageView) view.findViewById(R.id.iv_user_icon);
            holder.tvUserName= (TextView) view.findViewById(R.id.tv_username);
            holder.tvTime= (TextView) view.findViewById(R.id.tv_time);
            holder.tvContent= (TextView) view.findViewById(R.id.tv_content);
            holder.mImageView= (ImageView) view.findViewById(R.id.imageView);
            holder.ivComment= (ImageView) view.findViewById(R.id.iv_comment);
            holder.ivZan= (ImageView) view.findViewById(R.id.iv_zan);
            holder.tvZanNum= (TextView) view.findViewById(R.id.tv_zan);

            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        Topic  topic=mTopicList.get(position);

        holder.tvUserName.setText(topic.getUserName());
        holder.tvContent.setText(topic.getContent());
        holder.tvTime.setText(topic.getCreatedAt());
        x.image().bind(holder.mImageView, topic.getImage().getFileUrl(context), imageOptions);
        x.image().bind(holder.ivUserHead, topic.getImage().getFileUrl(context), imageOptions);

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.ToastShort("加油,少年!勇者无敌");
            }
        });

        return view;
    }

    class  ViewHolder{
        private  ImageView ivUserHead;
        private  TextView tvUserName;//用户名
        private  TextView  tvTime;//动态时间
        private  TextView  tvContent;//动态内容
        private  ImageView mImageView;//单图用一个ImageView展示
        private    ImageView ivComment;//评论按钮
        private    ImageView  ivZan;//点赞按钮
        private   TextView  tvZanNum;//点赞数量
    }
}
