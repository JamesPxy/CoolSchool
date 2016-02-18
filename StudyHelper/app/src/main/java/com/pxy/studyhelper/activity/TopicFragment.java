package com.pxy.studyhelper.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pxy.studyhelper.R;
import com.pxy.studyhelper.adapter.TopicAdapter;
import com.pxy.studyhelper.biz.GetTopicBiz;
import com.pxy.studyhelper.entity.Topic;
import com.pxy.studyhelper.utils.DialogUtil;

import java.util.List;

/**
 * 展示动态fragemnt
 */
public class TopicFragment extends Fragment {

    private PullToRefreshListView   mListView;
    private TopicAdapter  mTopicAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_topic,null);

        setListView(view);

        DialogUtil.showProgressDialog(getActivity(), "正在拼命加载数据...");
        GetTopicBiz.getTopicInfo(this);

        return view;
    }

    private void setListView(View view) {
        mListView= (PullToRefreshListView) view.findViewById(R.id.listView);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        //刷新时可以滚动listView
        mListView.setScrollingWhileRefreshingEnabled(true);
//        mListView.setShowIndicator(true);
        mListView.setShowViewWhileRefreshing(true);
        ILoadingLayout startLabels = mListView.getLoadingLayoutProxy(true, true);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("松开刷新...");// 下拉达到一定距离时，显示的提示

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                Toast.makeText(getActivity(), "下拉刷新...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Toast.makeText(getActivity(), "上拉刷新...", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void updateListView(List<Topic> list) {
        mTopicAdapter=new TopicAdapter(getActivity(),list);
        mListView.setAdapter(mTopicAdapter);
        DialogUtil.closeProgressDialog();
    }
}
