package com.pxy.studyhelper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pxy.studyhelper.R;
import com.pxy.studyhelper.entity.Comment;
import com.pxy.studyhelper.utils.Tools;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by ljy on 2016-02-19.
 */
public class CommentAdapter extends BaseAdapter {
    private Context context;
    private List<Comment> mCommentList;
    private ImageOptions imageOptions;
    public CommentAdapter(Context  context,List<Comment>  list){
        this.context=context;
        if(list!=null){
            mCommentList=list;
        }else{
            mCommentList=new ArrayList<>();
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
        return mCommentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCommentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder  holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_comment,null);
            holder.ivUserHead= (ImageView) convertView.findViewById(R.id.iv_user_icon);
            holder.tvUserName= (TextView) convertView.findViewById(R.id.tv_username);
            holder.tvTime= (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvContent= (TextView) convertView.findViewById(R.id.tv_content);
            holder.tvZan= (TextView) convertView.findViewById(R.id.tv_zan);
            holder.ivZan= (ImageView) convertView.findViewById(R.id.iv_zan);
            holder.mRelativeLayout= (RelativeLayout) convertView.findViewById(R.id.rv_user_info);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        final Comment  mComment=mCommentList.get(position);

        holder.tvUserName.setText(mComment.getUserName());
        holder.tvTime.setText(mComment.getCreatedAt());
        holder.tvContent.setText(mComment.getContent());
        holder.ivZan.setImageResource(R.drawable.love_icon);
        holder.tvZan.setText("赞(" + mComment.getLove() + ")");
        holder.ivZan.setTag(false);

        x.image().bind(holder.ivUserHead, mComment.getHeadUrl(), imageOptions);

        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((boolean)holder.ivZan.getTag()){
                    Tools.ToastShort("已赞过...");
                    return;
                }
                holder.ivZan.setImageResource(R.drawable.love_down_press_small);
                mComment.setLove(mComment.getLove().intValue() + 1);
                mComment.update(context, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        holder.tvZan.setText("赞(" + mComment.getLove() + ")");
                        holder.ivZan.setTag(true);
                        Tools.ToastShort("点赞成功...");
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        Tools.ToastShort("error--" + s);
                    }
                });
            }
        });

        return convertView;
    }

    class ViewHolder{
        private  ImageView ivUserHead;
        private TextView tvUserName;//用户名
        private  TextView  tvTime;//评论时间
        private  TextView  tvContent;//评论内容
        private  TextView  tvZan;//点赞数量
        private ImageView  ivZan;//点赞按钮
        private RelativeLayout  mRelativeLayout;

    }
}
