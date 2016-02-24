package com.pxy.studyhelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pxy.studyhelper.MyApplication;
import com.pxy.studyhelper.R;
import com.pxy.studyhelper.entity.User;
import com.pxy.studyhelper.fragment.SettingFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(value = R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @ViewInject(value = R.id.toolbar)
    private Toolbar  toolbar;
    @ViewInject(value = R.id.drawer_layout)
    private  DrawerLayout drawer;
    @ViewInject(value = R.id.nav_view)
    private  NavigationView navigationView;
    @ViewInject(value = R.id.radioGroup)
    private RadioGroup  mRadioGroup;
    @ViewInject(value = R.id.viewpager)
    private ViewPager  mViewPager;

    private ImageView  mIvUserPhoto;
    private TextView  tvUserName;
    private TextView  tvSign;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private List<Fragment> mFragmentList=new ArrayList<>();
    private User  mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        //初始化view
        initView();


    }

    private void initView() {
        //        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view=navigationView.getHeaderView(0);
        mIvUserPhoto= (ImageView) view.findViewById(R.id.img_user_photo);
        tvUserName= (TextView) view.findViewById(R.id.tv_user_name);
        tvSign= (TextView) view.findViewById(R.id.tv_sign);
        if(MyApplication.mCurrentUser==null)mUser=new User();
        else mUser=MyApplication.mCurrentUser;
        x.image().bind(mIvUserPhoto,mUser.getHeadUrl(), MyApplication.imageOptions);
        tvUserName.setText(mUser.getUsername());
        tvSign.setText(mUser.getSign());

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_index:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.rb_msg:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.rb_search:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.rb_mine:
                        mViewPager.setCurrentItem(3);
                        break;
                }
            }
        });

        mFragmentList.add(new TestBigFragment());
        mFragmentList.add(new TopicFragment());
        mFragmentList.add(new TestBigFragment());
        mFragmentList.add(new SettingFragment());
//        mFragmentList.add(new TopicFragment());

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);//多缓存一个页面
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_topic) {
            startActivity(new Intent(MainActivity.this,MakeTopicActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // 处理侧滑菜单点击事件
        int id = item.getItemId();
        if (id == R.id.nav_test_center) {//试题中心
            startActivity(new Intent(MainActivity.this,TestBigActivity.class));
        } else if (id == R.id.nav_collection) {//我的收藏
            startActivity(new Intent(MainActivity.this,MyCollectionActivity.class));

        } else if (id == R.id.nav_wrong_test) {//错题中心
            startActivity(new Intent(MainActivity.this,TestBigActivity.class));
        } else if (id == R.id.nav_setting) {//我的设置

        } else if (id == R.id.nav_challenge) {//挑战自我
            startActivity(new Intent(MainActivity.this,LoginActivity.class));

        } else if (id == R.id.nav_update) {//检查升级

        } else if (id == R.id.nav_back) {//问题反馈

        }else if(id==R.id.nav_share){//一键分享

        }
        //关闭侧滑菜单
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "动态";
//                case 1:
//                    return "题库";
//                case 2:
//                    return "我的";
//            }
//            return null;
//        }
    }



}
