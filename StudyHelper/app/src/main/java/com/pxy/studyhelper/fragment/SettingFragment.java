package com.pxy.studyhelper.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pxy.studyhelper.MyApplication;
import com.pxy.studyhelper.R;
import com.pxy.studyhelper.activity.LoginActivity;
import com.pxy.studyhelper.activity.MyTopicActivity;
import com.pxy.studyhelper.activity.PersonCenterActivity;
import com.pxy.studyhelper.activity.SettingActivity;
import com.pxy.studyhelper.entity.User;

import org.xutils.x;

import cn.bmob.v3.BmobUser;

/**
 * 我的fragemnt
 */
public class SettingFragment extends Fragment {

    private View  rootView;

    private RelativeLayout  rv_me;
    private RelativeLayout  rv_topic;
    private RelativeLayout  rv_setting;
    private Button  btn_logout;
    private TextView  tvName;
    private TextView  tvTitle;
    private ImageView  mImageView;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_setting, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeAllViewsInLayout();
        }

        initView(rootView);

        return rootView;
    }

    private void initView(View view) {
        rv_me= (RelativeLayout) view.findViewById(R.id.rv_mine);
        rv_topic= (RelativeLayout) view.findViewById(R.id.rv_my_topic);
        rv_setting= (RelativeLayout) view.findViewById(R.id.rv_setting);
        btn_logout= (Button) view.findViewById(R.id.btn_cancel);
        tvName= (TextView) view.findViewById(R.id.tv_name);
        tvTitle= (TextView) view.findViewById(R.id.tv_level);
        mImageView= (ImageView) view.findViewById(R.id.iv_head);



        rv_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.mCurrentUser!=null) {
                    Intent intent =new Intent(getActivity(),PersonCenterActivity.class);
                    intent.putExtra("from",true);
                    intent.putExtra("user",MyApplication.mCurrentUser);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.mCurrentUser!=null) {
//                    BmobUser.logOut(getActivity());   //清除缓存用户对象
                    MyApplication.mInstance.logout();
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                    tvName.setText("登录/注册");
//                    x.image().bind(mImageView, null, MyApplication.imageOptions);
//                    tvTitle.setText("");
//                    btn_logout.setText("立即登录");

                }else{
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });


        //查看我的动态
        rv_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.mCurrentUser==null){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getActivity().finish();
                }else {
                    Intent i = new Intent(getActivity(), MyTopicActivity.class);
                    i.putExtra("userId", MyApplication.mCurrentUser.getObjectId());
                    startActivity(i);
                }
            }
        });

        rv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SettingActivity.class));
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if(MyApplication.mCurrentUser==null){
            btn_logout.setText("立即登录");
        }else{
            //刷新当前用户个人信息
            MyApplication.mCurrentUser= BmobUser.getCurrentUser(MyApplication.mInstance, User.class);
            tvName.setText(MyApplication.mCurrentUser.getUsername());
            x.image().bind(mImageView, MyApplication.mCurrentUser.getHeadUrl(), MyApplication.imageOptions);
            tvTitle.setText("");
//            tvTitle.setText("王者荣耀"+MyApplication.mCurrentUser.getLevel());
            //todo  称号系统
//            tvTitle.setText(MyApplication.mCurrentUser.getLevel());
        }
    }
}
