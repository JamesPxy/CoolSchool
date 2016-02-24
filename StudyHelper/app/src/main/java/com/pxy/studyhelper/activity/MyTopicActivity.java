package com.pxy.studyhelper.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pxy.studyhelper.R;
import com.pxy.studyhelper.adapter.TopicAdapter;
import com.pxy.studyhelper.entity.Topic;
import com.pxy.studyhelper.utils.DialogUtil;
import com.pxy.studyhelper.utils.Tools;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by ljy on 2016-02-24.
 */
@ContentView(value = R.layout.activity_my_topic)
public class MyTopicActivity extends Activity {
    @ViewInject(value = R.id.listView)
    private PullToRefreshListView mListView;
    @ViewInject(value = R.id.iv_back)
    private ImageView  iv_back;
    @ViewInject(value = R.id.tv_title)
    private TextView title;
    @ViewInject(value = R.id.tv_alert)
    private TextView tvAlert;

    private TopicAdapter mTopicAdapter;

    private LinkedList<Topic> mTopicList=new LinkedList<>();
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        userId=getIntent().getStringExtra("userId");
        setTitle();
        setListView();
        queryData(curPage);
    }

    private void setTitle() {
        title.setText("我的动态");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setListView() {
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        //刷新时可以滚动listView
        mListView.setScrollingWhileRefreshingEnabled(true);
        mListView.setShowViewWhileRefreshing(true);
        ILoadingLayout startLabels = mListView.getLoadingLayoutProxy(true, true);
        startLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("松开刷新...");// 下拉达到一定距离时，显示的提示

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 下拉刷新(从第一页开始装载数据)
//                queryData(0, STATE_REFRESH);
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 上拉加载更多(加载下一页数据)
                queryData(curPage);
            }
        });
    }
    private  void updateListView(){
        if(mTopicAdapter==null) {
            mTopicAdapter = new TopicAdapter(MyTopicActivity.this, mTopicList);
            mListView.setAdapter(mTopicAdapter);
            DialogUtil.closeProgressDialog();
        }else{
            mTopicAdapter.notifyDataSetChanged();
        }
    }


    private int limit = 2;		//每次查询个数  每页显示个数
    private int curPage =0;		// 当前页的编号，从0开始

    /**
     * 分页获取数据
     * @param page	页码
     * @param actionType	ListView的操作类型（下拉刷新、上拉加载更多）
     */
    private void queryData( int page){
        BmobQuery<Topic> query = new BmobQuery<>();

        // 跳过之前页数并去掉重复数据
        query.setSkip(page * limit);
        //查询我的数据
        query.addWhereEqualTo("userId", userId);
        // 设置每页数据个数
        query.setLimit(limit);
        // 按时间升序查询
        query.order("createdAt");
        // 查找数据
        query.findObjects(MyTopicActivity.this, new FindListener<Topic>() {
            @Override
            public void onSuccess(List<Topic> list) {
                if(list.size()>0){
                    // 将本次查询的数据添加到mTopicList中
                    for (Topic td : list) {
                        LogUtil.i("STATE_more--666--"+td.toString());
                        mTopicList.addLast(td);
                    }
                    curPage++;
                    updateListView();
                    LogUtil.i("666---第"+curPage+"页数据加载完成");
                }else{
                    Tools.ToastShort("没有更多数据了");
                    tvAlert.setVisibility(View.VISIBLE);
                }
                mListView.onRefreshComplete();
            }

            @Override
            public void onError(int arg0, String arg1) {
                Tools.ToastShort(arg0+"查询失败:"+arg1);
                mListView.onRefreshComplete();
            }
        });
    }


}
