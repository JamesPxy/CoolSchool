package com.pxy.studyhelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pxy.studyhelper.MyApplication;
import com.pxy.studyhelper.R;
import com.pxy.studyhelper.activity.CommentActivity;
import com.pxy.studyhelper.activity.ImageBrowserActivity;
import com.pxy.studyhelper.activity.PersonCenterActivity;
import com.pxy.studyhelper.entity.Topic;
import com.pxy.studyhelper.utils.Tools;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.UpdateListener;

import static com.pxy.studyhelper.R.id.iv_zan;

/**
 * User: Pxy(15602269883@163.com)
 * Date: 2016-02-16
 * Time: 15:30
 * FIXME
 */
public class TopicAdapter  extends BaseAdapter {
    private Context  context;
    private List<Topic>  mTopicList;

    public TopicAdapter(Context  context,List<Topic>  mTopicList){
        this.context=context;
        if(mTopicList!=null){
            this.mTopicList=mTopicList;
        }else{
            this.mTopicList=new ArrayList<>();
        }

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
            holder.lvComment = (LinearLayout) view.findViewById(R.id.lv_comment);
            holder.lvZan = (LinearLayout) view.findViewById(R.id.lv_zan);
            holder.ivZan= (ImageView) view.findViewById(iv_zan);
            holder.tvZanNum= (TextView) view.findViewById(R.id.tv_zan);
            holder.lvTopic= (LinearLayout) view.findViewById(R.id.lv_topic);

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
            x.image().bind(holder.mImageView, topic.getImage().getFileUrl(context));
        }else{
            holder.mImageView.setVisibility(View.GONE);
        }
        x.image().bind(holder.ivUserHead, topic.getHeadUrl(), MyApplication.imageOptions);

        holder.ivUserHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016-03-01 跳转至好友空间  个人中心
                String userId=topic.getUserId();
                Intent intent =new Intent(context,PersonCenterActivity.class);
                if(userId!=MyApplication.mCurrentUser.getObjectId()){
                    intent.putExtra("from",false);
                    intent.putExtra("chat",true);
                    intent.putExtra("name",topic.getUserName());
                    context.startActivity(intent);
                }else{
                    intent.putExtra("from",true);
                    intent.putExtra("user",MyApplication.mCurrentUser);
                    context.startActivity(intent);
                }
            }
        });

        holder.lvTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查看评论
                Intent  intent=new Intent(context, CommentActivity.class);
                intent.putExtra("topic",topic);
                context.startActivity(intent);
            }
        });
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 查看大图 我自己写的
//                Intent  intent=new Intent(context,ShowTopicDetail.class);
//                intent.putExtra("topic",topic);
//                context.startActivity(intent);
                // TODO 浏览图片
                Intent intent =new Intent(context,ImageBrowserActivity.class);
                ArrayList<String> photos = new ArrayList<String>();
                photos.add(topic.getImage().getFileUrl(context));
                intent.putStringArrayListExtra("photos", photos);
                intent.putExtra("position", 0);
                context.startActivity(intent);

            }
        });
        holder.lvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("topic", topic);
                context.startActivity(intent);
            }
        });
        holder.lvZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((boolean) holder.ivZan.getTag()) {
                    Tools.ToastShort("已经赞过");
                    return;
                }
                topic.setLove(topic.getLove().intValue() + 1);
                topic.update(context, new UpdateListener() {
                    @Override
                    public void onSuccess() {
//                        Tools.ToastShort("点赞成功...");
                        holder.ivZan.setTag(true);
                        LogUtil.i("love ---" + topic.getLove().intValue());
                        LogUtil.i("love --1---" + (topic.getLove().intValue() + 1));
                        holder.tvZanNum.setText(topic.getLove() + "");
                        holder.ivZan.setImageResource(R.drawable.love_down_press_small);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Tools.ToastShort("点赞失败..." + s);
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
        private    LinearLayout lvComment;//评论
        private    LinearLayout lvZan;//点赞
        private    ImageView ivZan;//点赞按钮
        private   TextView  tvZanNum;//点赞数量
        private LinearLayout  lvTopic;//整个item
    }
}
