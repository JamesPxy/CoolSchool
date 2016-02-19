package com.pxy.studyhelper.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
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

    private CommentAdapter  mCommentAdapter;
    private LinkedList<Comment>  mCommentList=new LinkedList<>();
    private int  curPage=0;

    private Topic mTopic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        iniitView();

        mTopic= (Topic) getIntent().getSerializableExtra("topic");
        if(mTopic==null){
            Tools.ToastShort("topic  null");
            this.finish();
        }

        DialogUtil.showProgressDialog(this,"努力加载中...");
        getCommentInfo(this, mTopic.getObjectId(), curPage);
    }

    private void iniitView() {
        tvTitle.setText("评论详情");

        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        //刷新时可以滚动listView
        mListView.setScrollingWhileRefreshingEnabled(true);
//        mListView.setShowIndicator(true);
        mListView.setShowViewWhileRefreshing(true);
        ILoadingLayout startLabels = mListView.getLoadingLayoutProxy(true, true);
        startLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("松开刷新...");// 下拉达到一定距离时，显示的提示
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
            DialogUtil.closeProgressDialog();
        }else{
            mCommentAdapter.notifyDataSetChanged();
            mListView.onRefreshComplete();
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
        query.addWhereEqualTo("topicId", topicId);
        query.setLimit(2);
        query.setSkip(2 * page);
        query.order("createdAt");
        query.findObjects(context, new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {
                LogUtil.i("get comment success--"+list.size());
                if(list.size()>0){
                    for(Comment  c:list){
                        mCommentList.addLast(c);
                    }
                    updateListView();
                }else{
                    Tools.ToastShort("list  size  0");
                    mListView.onRefreshComplete();
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.e(i + "get  comment  info  error--" + s);
                Tools.ToastShort("error--"+s);
            }
        });
    }


    private void uploadCommentInfo(Context context){
        String  str=mEdtComment.getText().toString().trim();
        if(TextUtils.isEmpty(str)){
            Tools.ToastShort("评论内容不能为空...");
            return;
        }
        Comment  comment=new Comment();
        comment.setContent(str);
        comment.setTopicId(mTopic.getObjectId());
        //todo  获取当前用户
        comment.setUserName("username");
        comment.setHeadUrl("null");

        comment.setLove(0);

        comment.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Tools.ToastShort("发送评论成功...");
            }

            @Override
            public void onFailure(int i, String s) {
                Tools.ToastShort("发送评论失败.."+s);
            }
        });
    }

}
