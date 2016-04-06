package com.pxy.studyhelper.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bmob.BmobPro;
import com.pxy.studyhelper.MyApplication;
import com.pxy.studyhelper.R;
import com.pxy.studyhelper.entity.User;
import com.pxy.studyhelper.fragment.ContactFragment;
import com.pxy.studyhelper.fragment.RecentFragment;
import com.pxy.studyhelper.fragment.TestBigFragment;
import com.pxy.studyhelper.fragment.TopicFragment;
import com.pxy.studyhelper.utils.Constant;
import com.pxy.studyhelper.utils.Tools;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by:Pxy
 * Date: 2016-01-04
 * Time: 10:36
 * 应用主界面
 */
@ContentView(value = R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    @ViewInject(value = R.id.toolbar)
    private Toolbar toolbar;
    @ViewInject(value = R.id.drawer_layout)
    private DrawerLayout drawer;
    @ViewInject(value = R.id.nav_view)
    private NavigationView navigationView;
    @ViewInject(value = R.id.radioGroup)
    private RadioGroup mRadioGroup;
    @ViewInject(value = R.id.viewpager)
    private ViewPager mViewPager;
    @ViewInject(value = R.id.rb_index)
    private RadioButton rbIndex;
    @ViewInject(value = R.id.rb_msg)
    private RadioButton rbMsg;
    @ViewInject(value = R.id.rb_search)
    private RadioButton rbSearch;
    @ViewInject(value = R.id.rb_mine)
    private RadioButton rbMine;


    private ImageView mIvUserPhoto;
    private TextView tvUserName;
    private TextView tvSign;
    private TextView tvSignIn;//签到

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private User mUser;
    private RadioButton[] mRadioButton = new RadioButton[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        x.view().inject(this);
        //初始化view
        initView();
        LogUtil.i("on create");

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        LogUtil.i(" onCreateView");
        return super.onCreateView(name, context, attrs);
    }

    private void initView() {
        mRadioButton[0] = rbIndex;
        mRadioButton[1] = rbSearch;
        mRadioButton[2] = rbMsg;
        mRadioButton[3] = rbMine;

//        mRadioButton=
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        mRadioGroup= (RadioGroup) findViewById(R.id.radioGroup);
//        mViewPager= (ViewPager) findViewById(R.id.viewpager);

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        mIvUserPhoto = (ImageView) view.findViewById(R.id.img_user_photo);
        tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
        tvSign = (TextView) view.findViewById(R.id.tv_signs);
        tvSignIn = (TextView) view.findViewById(R.id.tv_sign_in);

//        if(MyApplication.mCurrentUser==null)mUser=new User();
//        else mUser=MyApplication.mCurrentUser;
//        x.image().bind(mIvUserPhoto, mUser.getHeadUrl(), MyApplication.imageOptions);
//        tvUserName.setText(mUser.getUsername());
//        // TODO: 2016-03-21 称号系统
//        tvSign.setText("level: "+Constant.level[mUser.getLevel() + 2]+"\n"+mUser.getSign());

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_index:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.rb_search:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.rb_msg:
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
        mFragmentList.add(new RecentFragment());
        mFragmentList.add(new ContactFragment());
//        mFragmentList.add(new SettingFragment());


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);//多缓存一个页面
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(1);
//        mRadioButton[1].setChecked(true);
//        mViewPager.setSystemUiVisibility(0);
//        mFragmentList.get(1).setUserVisibleHint(false);//取消预加载

        mIvUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PersonCenterActivity.class);
                intent.putExtra("from", true);
                intent.putExtra("user", MyApplication.mCurrentUser);
                startActivity(intent);
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int score = MyApplication.mCurrentUser.getScore();
                tvSignIn.setText("已签到：" + score);
                MyApplication.mCurrentUser.setScore(score + 10);
                tvSignIn.setEnabled(false);
                Tools.ToastShort("签到成功");
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (MyApplication.mCurrentUser != null) {
            mUser = MyApplication.mCurrentUser;
            tvUserName.setText(mUser.getUsername());
            tvSign.setText("等级: " + Constant.level[mUser.getLevel() + 2] + "\n" + mUser.getSign());
            x.image().bind(mIvUserPhoto, MyApplication.mCurrentUser.getHeadUrl(), MyApplication.imageOptions);
        }
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage("是否立即退出?")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
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
            startActivity(new Intent(MainActivity.this, MakeTopicActivity.class));
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
            startActivity(new Intent(MainActivity.this, TestBigActivity.class));
        } else if (id == R.id.nav_collection) {//我的收藏
            startActivity(new Intent(MainActivity.this, MyCollectionActivity.class));

        } else if (id == R.id.nav_wrong_test) {//错题中心
            startActivity(new Intent(MainActivity.this, TestBigActivity.class));
        } else if (id == R.id.nav_setting) {//我的设置
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        } else if (id == R.id.nav_challenge) {//我的个人动态
            ToMyTopic();
        } else if (id == R.id.nav_update) {//检查升级
            check4Update();

        } else if (id == R.id.nav_back) {//问题反馈
            startActivity(new Intent(MainActivity.this, ActivityFeedBack.class));

        } else if (id == R.id.nav_share) {//一键分享
//            Tools.ToastShort("有待进一步开发,敬请期待...");
            doShareBiz();
        } else if (id == R.id.nav_clear_cache) {//清除缓存
            new AlertDialog.Builder(this).setTitle("提示")
                    .setMessage("是否立即清除缓存")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            clearCache();
                        }
                    }).show();
        }

        //关闭侧滑菜单
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void doShareBiz() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");  //纯文本
//　　　　it.setType("image/png");
//　　　　　//添加图片
//　　　　 File f = new File(Environment.getExternalStorageDirectory()
//　　　　　　 +"/Pictures/2.png");
//　　　　 Uri u = Uri.fromFile(f);
//　　　　 it.putExtra(Intent.EXTRA_STREAM, u);
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, "分享一个超赞的中医药学习类辅助软件给你，赶紧链接下载吧：http://pxy.study_helper.com");
        startActivity(Intent.createChooser(intent, getTitle()));
    }

    private void check4Update() {
        try {
            // TODO: 2016-03-17 获取网络编号versioncode  检查更新升级
            if (getPackageManager().getPackageInfo(getPackageName(), 0).versionCode < 3) {
                new AlertDialog.Builder(this)
                        .setTitle("更新提示")
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage("发现新版内容，修复bug，增加人机对战,是否立即更新")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", null)
                        .show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("更新提示")
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage("当前已是最新版本，无需更新")
                        .setPositiveButton("确定", null)
                        .show();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除缓存
     */
    private void clearCache() {
        //文件大小（单位：字节）
        String cacheSize = String.valueOf(BmobPro.getInstance(this).getCacheFileSize());
        //对文件大小进行格式化，转化为'B'、'K'、'M'、'G'等单位
        String formatSize = BmobPro.getInstance(this).getCacheFormatSize();
        LogUtil.i(formatSize);
        //        清除缓存：
//        BmobPro.getInstance(this).clearCache();
    }

    private void ToMyTopic() {
        if (MyApplication.mCurrentUser == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Intent i = new Intent(MainActivity.this, MyTopicActivity.class);
            i.putExtra("userId", MyApplication.mCurrentUser.getObjectId());
            startActivity(i);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mRadioButton[position].setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
//        public int getItemPosition(Object object) {
//            return POSITION_NONE;
//        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            LogUtil.i("---destroyItem---"+position);
//            if(position==1) {
//                LogUtil.i("---destroyItem---"+position);
//                super.destroyItem(container, position, object);
//            }
        }
    }


}
