package com.pxy.studyhelper.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pxy.studyhelper.R;
import com.pxy.studyhelper.adapter.TopicAdapter;
import com.pxy.studyhelper.biz.GetTopicBiz;
import com.pxy.studyhelper.entity.Topic;
import com.pxy.studyhelper.utils.DialogUtil;
import com.pxy.studyhelper.utils.Tools;

import org.xutils.common.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

/**
 * 展示动态fragemnt
 */
public class TopicFragment extends Fragment {

    private PullToRefreshListView   mListView;
    private TopicAdapter  mTopicAdapter;
    private GetTopicBiz  mGetTopicBiz;
    private LinkedList<Topic> mTopicList=new LinkedList<>();
    private int mCurrentPage=0;
    private boolean  isNewExist=true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_topic,null);

        setListView(view);

        DialogUtil.showProgressDialog(getActivity(), "正在拼命加载数据...");
//        mGetTopicBiz=new GetTopicBiz();
//        mGetTopicBiz.getTopicInfo(this,mCurrentPage);
//        getTopicInfo(getActivity(),mCurrentPage);
        queryData(0, STATE_REFRESH);
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
//                mGetTopicBiz.getTopicInfo(TopicFragment.this, mCurrentPage++);
//                if(isNewExist){
//                    getTopicInfo(getActivity(), ++mCurrentPage);
//                }else{
//                    Tools.ToastShort("已是最新数据,没有更多了...");
//                    mListView.onRefreshComplete();
//                }
                // 下拉刷新(从第一页开始装载数据)
                queryData(0, STATE_REFRESH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                mGetTopicBiz.getTopicInfo(TopicFragment.this, mCurrentPage++);
//                getTopicInfo(getActivity(),++mCurrentPage);
                // 上拉加载更多(加载下一页数据)
                queryData(curPage, STATE_MORE);
            }
        });

//        ListView mMsgListView = mListView.getRefreshableView();
//        // 再设置adapter
//        mMsgListView.setAdapter(new TopicAdapter(getActivity(), mTopicList));
//        mTopicAdapter.notifyDataSetChanged();

    }
    private  void updateview(){
        if(mTopicAdapter==null) {
            mTopicAdapter = new TopicAdapter(getActivity(), mTopicList);
            mListView.setAdapter(mTopicAdapter);
            DialogUtil.closeProgressDialog();
        }else{
            mTopicAdapter.notifyDataSetChanged();
        }
        mListView.onRefreshComplete();
    }

    public void updateListView(List<Topic> list) {
        if(mTopicAdapter==null) {
            mTopicList.addAll(list);
            mTopicAdapter = new TopicAdapter(getActivity(), mTopicList);
            mListView.setAdapter(mTopicAdapter);
        }else{
            for(Topic  topic1:list){
                mTopicList.addFirst(topic1);
            }
            mTopicAdapter.notifyDataSetChanged();
            mListView.onRefreshComplete();
        }
        DialogUtil.closeProgressDialog();
    }

    public  void  getTopicInfo(Context  context,int page){
        BmobQuery<Topic> mTopicQuery =new BmobQuery<>();
        mTopicQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);//先从网络读取数据，如果没有，再从缓存中获取。
        //按时间  从最新动态  到之前发布动态
        mTopicQuery.order("-createdAt");
        // 返回50条数据，如果不加上这条语句，默认返回10条数据
        mTopicQuery.setLimit(3);
//        query.setSkip(10); // 忽略前10条数据（即第一页数据结果）
        mTopicQuery.setSkip(3*page);
        mTopicQuery.findObjects(context, new FindListener<Topic>() {
            @Override
            public void onSuccess(List<Topic> list) {
                if (list != null) updateListView(list);
                Tools.ToastShort(list.size() + list.get(0).toString());
                LogUtil.i("get topic Info  666 success -----" + list.size() + "---"+list.get(0).toString());
            }

            @Override
            public void onError(int i, String s) {
                if(i==9015){
                    isNewExist=false;
                    Tools.ToastShort("已是最新数据,没有更多了...");
                }else{
                    Tools.ToastShort(s);
                }
//                if (i == 9009) {//todo No cache data 没有缓存数据  9015error----Invalid index 0, size is 0
//                }
                LogUtil.e(i + "error----" + s);
                //todo 处理错误情况
                mListView.onRefreshComplete();
            }
        });
    }


    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多

    private int limit = 4;		//每一页显示个数
    private int curPage =0;		// 当前页的编号，从0开始
    private String  lastTime;   //最后动态时间

    /**
     * 分页获取数据
     * @param page	页码
     * @param actionType	ListView的操作类型（下拉刷新、上拉加载更多）
     */
    private void queryData( int page, final int actionType){
        Log.i("bmob", "pageN:" + page + " limit:" + limit + " actionType:" + actionType);

        BmobQuery<Topic> query = new BmobQuery<>();
        // 按时间降序查询
        query.order("-createdAt");
        // 如果是加载更多
        if(actionType == STATE_MORE){
            // 处理时间查询
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(lastTime);
            } catch (Exception e) {
                LogUtil.e(e.getMessage());
            }
            // 只查询小于等于最后一个item发表时间的数据
            query.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
            // 跳过之前页数并去掉重复数据
            query.setSkip(page * limit);
        }else{
            page=0;
            query.setSkip(page);
        }
        // 设置每页数据个数
        query.setLimit(limit);
        // 查找数据
        query.findObjects(getActivity(), new FindListener<Topic>() {
            @Override
            public void onSuccess(List<Topic> list) {
                if(list.size()>0){
                    if(actionType == STATE_REFRESH){
                        // 当是下拉刷新操作时，将当前页的编号重置为0，并把mTopicList清空，重新添加
                        curPage = 0;
                        mTopicList.clear();
                        // 获取最后时间
                        lastTime = list.get(list.size()-1).getCreatedAt();
                        LogUtil.i("last time------"+lastTime);
                    }
                    // 将本次查询的数据添加到mTopicList中
                    for (Topic td : list) {
                        LogUtil.i(td.toString());
                        mTopicList.add(td);
                    }
                    updateview();
                    curPage++;
                    Tools.ToastShort("第"+curPage+"页数据加载完成");
                }else if(actionType == STATE_MORE){
                    Tools.ToastShort("没有更多数据了");
                }else if(actionType == STATE_REFRESH){
                    Tools.ToastShort("没有最新数据了");
                }
            }

            @Override
            public void onError(int arg0, String arg1) {
                Tools.ToastShort("查询失败:"+arg1);
                mListView.onRefreshComplete();
            }
        });
    }

}
