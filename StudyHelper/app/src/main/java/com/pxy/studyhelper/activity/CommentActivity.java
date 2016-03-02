package com.pxy.studyhelper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pxy.studyhelper.MyApplication;
import com.pxy.studyhelper.R;
import com.pxy.studyhelper.adapter.CommentAdapter;
import com.pxy.studyhelper.entity.Comment;
import com.pxy.studyhelper.entity.Topic;
import com.pxy.studyhelper.utils.DialogUtil;
import com.pxy.studyhelper.utils.Tools;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

@ContentView(value = R.layout.activity_comment)
public class CommentActivity extends AppCompatActivity {

    @ViewInject(value = R.id.pull_listview)
    private PullToRefreshListView  mListView;
    @ViewInject(value = R.id.edt_comment)
    private EditText  mEdtComment;
    @ViewInject(value = R.id.tv_title)
    private TextView  tvTitle;
    @ViewInject(value = R.id.tv_no_comment)
    private   TextView  tvNoComment;

    /**
     * 当没有评论是显示的动态详情
     *
     */
    @ViewInject(value = R.id.lv_topics)
    private LinearLayout  lvTopics;
    @ViewInject(value = R.id.iv_user_icon)
    private ImageView  ivUserHead;
    @ViewInject(value = R.id.tv_username)
    private TextView  tvUsername;
    @ViewInject(value = R.id.tv_time)
    private TextView  tvTime;
    @ViewInject(value = R.id.tv_content)
    private TextView  tvContent;
    @ViewInject(value = R.id.imageView)
    private  ImageView  ivTopic;

    private CommentAdapter  mCommentAdapter;
    private LinkedList<Comment>  mCommentList=new LinkedList<>();
    private int  curPage=0;
    private Topic mTopic;


    ImageView  head;
    TextView  name;
    TextView  time;
    TextView  content;
    ImageView  imageViews;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if(MyApplication.mCurrentUser==null){
            Tools.ToastShort("请先登录,再发表动态");
            finish();
        }


        mTopic= (Topic) getIntent().getSerializableExtra("topic");
        if(mTopic==null){
            this.finish();
        }

        DialogUtil.showProgressDialog(this, "努力加载中...");
        getCommentInfo(this, mTopic.getObjectId(), curPage);

        initView();
    }

    private void initView() {
        tvTitle.setText("动态详情");

        if(mCommentList.size()==0){//没有评论的情况下
            lvTopics.setVisibility(View.VISIBLE);
            x.image().bind(ivUserHead, mTopic.getHeadUrl(), MyApplication.imageOptions);
            tvUsername.setText(mTopic.getUserName());
            tvTime.setText(mTopic.getCreatedAt());
            tvContent.setText(mTopic.getContent());
            if(mTopic.getImage()!=null) {
                ivTopic.setVisibility(View.VISIBLE);
                x.image().bind(ivTopic, mTopic.getImage().getFileUrl(this));
            }else{
                ivTopic.setVisibility(View.GONE);
            }
        }
        ivTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 浏览图片
                Intent intent =new Intent(CommentActivity.this,ImageBrowserActivity.class);
                ArrayList<String> photos = new ArrayList<String>();
                photos.add(mTopic.getImage().getFileUrl(CommentActivity.this));
                intent.putStringArrayListExtra("photos", photos);
                intent.putExtra("position", 0);
                startActivity(intent);
            }
        });


        LayoutInflater mInflater=LayoutInflater.from(this);
        LinearLayout headView = (LinearLayout) mInflater.inflate(R.layout.topic_detail, null);
        ListView  listview=mListView.getRefreshableView();
        listview.addHeaderView(headView);
        head= (ImageView)headView.findViewById(R.id.user_head);
        name= (TextView) headView.findViewById(R.id.tv_username);
        time= (TextView) headView.findViewById(R.id.tv_time);
        content= (TextView) headView.findViewById(R.id.tv_content);
        imageViews= (ImageView) headView.findViewById(R.id.imageView);





        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        //刷新时可以滚动listView
        mListView.setScrollingWhileRefreshingEnabled(true);
//        mListView.setShowIndicator(true);
        mListView.setShowViewWhileRefreshing(true);
        ILoadingLayout endLables = mListView.getLoadingLayoutProxy(false, true);
        endLables.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLables.setRefreshingLabel("正在载入...");// 刷新时
        endLables.setReleaseLabel("松开刷新...");// 下拉达到一定距离时，显示的提示
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉刷新
                getCommentInfo(CommentActivity.this, mTopic.getObjectId(), ++curPage);
            }
        });
    }

    private void updateListView(){
        if(mCommentAdapter==null){
            mCommentAdapter=new CommentAdapter(this,mCommentList);
            mListView.setAdapter(mCommentAdapter);

            lvTopics.setVisibility(View.GONE);

            x.image().bind(head, mTopic.getHeadUrl(), MyApplication.imageOptions);
            name.setText(mTopic.getUserName());
            time.setText(mTopic.getCreatedAt());
            content.setText(mTopic.getContent());
            if (mTopic.getImage() != null) {
                imageViews.setVisibility(View.VISIBLE);
                x.image().bind(imageViews, mTopic.getImage().getFileUrl(this));
            } else {
                imageViews.setVisibility(View.GONE);
            }
            imageViews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO 浏览图片
                    Intent intent =new Intent(CommentActivity.this,ImageBrowserActivity.class);
                    ArrayList<String> photos = new ArrayList<String>();
                    photos.add(mTopic.getImage().getFileUrl(CommentActivity.this));
                    intent.putStringArrayListExtra("photos", photos);
                    intent.putExtra("position", 0);
                    startActivity(intent);
                }
            });

            DialogUtil.closeProgressDialog();
        }else{
            mCommentAdapter.notifyDataSetChanged();
        }
    }


    @Event(value = {R.id.iv_back,R.id.iv_send_conmment},type = View.OnClickListener.class)
    private void  doClick(View view){
        switch (view.getId()){
            case R.id.iv_back:this.finish();break;
            case R.id.iv_send_conmment:uploadCommentInfo(this);break;
        }
    }

    private void  getCommentInfo(Context context,String  topicId,int page){

        BmobQuery<Comment>   query=new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.addWhereEqualTo("topicId", topicId);
        query.setLimit(4);
        query.setSkip(4 * page);
        query.order("createdAt");
        query.findObjects(context, new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {
                LogUtil.i("get comment success--"+list.size());
                if(list.size()>0){
                    for(Comment  c:list){
                        mCommentList.addLast(c);
                    }
                    tvNoComment.setVisibility(View.GONE);
                    updateListView();
                }else{
                    //没有更多评论
                    tvNoComment.setVisibility(View.VISIBLE);
                }
                DialogUtil.closeProgressDialog();
                mListView.onRefreshComplete();
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.e(i + "get  comment  info  error--" + s);
                Tools.ToastShort("error--"+s);
            }
        });
    }


    private void uploadCommentInfo(final Context context){
        String  str=mEdtComment.getText().toString().trim();
        if(TextUtils.isEmpty(str)){
            Tools.ToastShort("评论内容不能为空...");
            return;
        }
        final Comment  comment=new Comment();
        comment.setContent(str);
        comment.setTopicId(mTopic.getObjectId());
        //todo  获取当前用户
        comment.setUserName(MyApplication.mCurrentUser.getUsername());
        comment.setHeadUrl(MyApplication.mCurrentUser.getHeadUrl());
        comment.setLove(0);

        comment.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Tools.ToastShort("发送评论成功...");
//                getCommentInfo(context, mTopic.getObjectId(), curPage);
                mCommentList.addLast(comment);
                updateListView();
            }

            @Override
            public void onFailure(int i, String s) {
                Tools.ToastShort("发送评论失败.."+s);
            }
        });
    }

}
