package com.pxy.studyhelper.biz;

import android.content.Context;

import com.pxy.studyhelper.entity.Topic;
import com.pxy.studyhelper.entity.User;
import com.pxy.studyhelper.fragment.TopicFragment;
import com.pxy.studyhelper.utils.Tools;

import org.xutils.common.util.LogUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * User: Pxy(15602269883@163.com)
 * Date: 2016-02-16
 * Time: 09:04
 * FIXME
 */
public class GetTopicBiz  {
    BmobQuery<Topic> mTopicQuery;
    public GetTopicBiz(){

    }


    public  void  getTopicInfo(final TopicFragment topicFragment,int page){
        mTopicQuery =new BmobQuery<>();
        mTopicQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);//先从网络读取数据，如果没有，再从缓存中获取。
        //按时间  从最新动态  到之前发布动态
        mTopicQuery.order("-createdAt");
        // 返回50条数据，如果不加上这条语句，默认返回10条数据
        mTopicQuery.setLimit(3);
//        query.setSkip(10); // 忽略前10条数据（即第一页数据结果）
        mTopicQuery.setSkip(3*page);
        mTopicQuery.findObjects(topicFragment.getActivity(), new FindListener<Topic>() {
            @Override
            public void onSuccess(List<Topic> list) {
                LogUtil.i("get topic Info  success  666---" + list.size()+list.get(0).toString());
//                if (list != null) topicFragment.updateListView(list);
//                else{LogUtil.e("get  topic  info  list  null");}
            }

            @Override
            public void onError(int i, String s) {
                if(i==9009){//No cache data 没有更多数据
//                    topicFragment.updateListView(new ArrayList<Topic>());
                }
                LogUtil.e(i + "error----" + s);
                Tools.ToastShort("error--"+s);
                //todo 处理错误情况
            }
        });
    }


    public  User queryUserInfoById(Context  context,String id){
        final User[] user = new User[1];
        BmobQuery<User>  query=new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);//先从网络读取数据，如果没有，再从缓存中获取。
        query.addWhereEqualTo("objectId", id);
        query.findObjects(context, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if(list.size()>0) user[0] =list.get(0);
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.e(i+"get userInfo  error"+s);
                user[0]=null;
            }
        });
        return user[0];

    }





}  
