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
import android.widget.RadioGroup;

import com.pxy.studyhelper.R;

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

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private List<Fragment> mFragmentList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
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

        mFragmentList.add(new TopicFragment());
        mFragmentList.add(new TestBigFragment());
        mFragmentList.add(new TestBigFragment());
        mFragmentList.add(new TopicFragment());

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);//多缓存一个页面
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        if (id == R.id.nav_camera) {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));

        } else if (id == R.id.nav_send) {

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
