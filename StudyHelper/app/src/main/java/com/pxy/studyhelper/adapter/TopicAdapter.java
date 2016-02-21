package com.pxy.studyhelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pxy.studyhelper.R;
import com.pxy.studyhelper.activity.CommentActivity;
import com.pxy.studyhelper.entity.Topic;
import com.pxy.studyhelper.utils.Tools;

import org.xutils.common.util.DensityUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.UpdateListener;

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
        final ViewHolder  holder;
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
        final Topic  topic=mTopicList.get(position);

        holder.tvUserName.setText(topic.getUserName());
        holder.tvContent.setText(topic.getContent());
        holder.tvTime.setText(topic.getCreatedAt());
        holder.tvZanNum.setText(topic.getLove().toString());
        holder.ivZan.setTag(false);
        holder.ivZan.setImageResource(R.drawable.love_icon);

        if(topic.getImage()!=null) {
            holder.mImageView.setVisibility(View.VISIBLE);
            x.image().bind(holder.ivUserHead, topic.getImage().getFileUrl(context), imageOptions);
            x.image().bind(holder.mImageView, topic.getImage().getFileUrl(context), imageOptions);
        }else{
            holder.mImageView.setVisibility(View.GONE);
        }

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent=new Intent(context, CommentActivity.class);
                intent.putExtra("topic",topic);
                context.startActivity(intent);
                //todo  展示大图
            }
        });
        holder.ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent=new Intent(context, CommentActivity.class);
                intent.putExtra("topic",topic);
                context.startActivity(intent);
            }
        });
        holder.ivZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((boolean)holder.ivZan.getTag()){
                    Tools.ToastShort("已经赞过");
                    return;
                }
                topic.setLove(topic.getLove().intValue()+1);
                topic.update(context, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Tools.ToastShort("点赞成功...");
                        holder.ivZan.setTag(true);
                        LogUtil.i("love ---"+topic.getLove().intValue());
                        LogUtil.i("love --1---"+(topic.getLove().intValue()+1));
                        holder.tvZanNum.setText(topic.getLove()+ "");
                        holder.ivZan.setImageResource(R.drawable.love_down_press_small);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Tools.ToastShort("点赞失败..."+s);
                    }
                });
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
