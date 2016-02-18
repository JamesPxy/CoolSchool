package com.pxy.studyhelper.biz;

import android.content.Context;

import com.pxy.studyhelper.activity.TopicFragment;
import com.pxy.studyhelper.entity.Topic;
import com.pxy.studyhelper.entity.User;

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

    public static void  getTopicInfo(final TopicFragment topicFragment){
        Context context=topicFragment.getActivity();
        BmobQuery<Topic> query=new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);//先从网络读取数据，如果没有，再从缓存中获取。
//        Calendar  calendar=Calendar.getInstance();
//        LogUtil.e(calendar.toString());
//        LogUtil.e("calender---"+calendar.get(DAY_OF_YEAR));
//        String start = "2016-02-14 00:00:00";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date  = null;
//        try {
//            date = sdf.parse(start);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        query.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(date));
        // 多个排序字段可以用（，）号分隔
        query.order("createdAt");
        // 返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        query.findObjects(context, new FindListener<Topic>() {
            @Override
            public void onSuccess(List<Topic> list) {
                LogUtil.i("get topicInfo  success  666"+list.size());
               if(list!=null) topicFragment.updateListView(list);
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.e(i+"error----"+s);
            }
        });
    }


    public static final  User queryUserInfoById(Context  context,String id){
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
