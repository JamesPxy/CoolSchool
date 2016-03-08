package com.pxy.studyhelper;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.pxy.studyhelper.entity.User;
import com.pxy.studyhelper.utils.CollectionUtils;
import com.pxy.studyhelper.utils.SharePreferenceUtil;

import org.xutils.DbManager;
import org.xutils.common.util.DensityUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.im.BmobChat;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * User: Pxy(15602269883@163.com)
 * Date: 2016-01-24
 * Time: 21:16
 * FIXME
 */
public class MyApplication  extends Application {

    public static MyApplication mInstance;

    public static User mCurrentUser;

    public static boolean isDebug = true;

    public static DbManager dbManager;

    public static DbManager.DaoConfig daoConfig;

    public static ImageOptions imageOptions;

    public LocationClient mLocationClient;

    public MyLocationListener mMyLocationListener;

    public BmobGeoPoint lastPoint = null;// 上一次定位到的经纬度

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //初始化xUtil
        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志

        //初始化相关api
        init();


        daoConfig = new DbManager.DaoConfig()
                .setDbName("FavoriteQuestion")//创建数据库的名称
                .setDbVersion(1)//数据库版本号
//                .setDbDir()//设置数据库存储路径  ，如果不设置，那么数据库默认存储在/data/data/你的应用程序/database/xxx.db下。
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        // TODO: ...
                        // db.addColumn(...);
                        // db.dropTable(...);
                    }
                });//数据库更新操作
        dbManager = x.getDb(daoConfig);

        mCurrentUser = BmobUser.getCurrentUser(this, User.class);
        if (mCurrentUser != null) {
            //缓存对象不为空时允许用户使用
            LogUtil.i(mCurrentUser.toString());
        }

        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))//图片大小
                .setRadius(DensityUtil.dip2px(360))//ImageView圆角半径
                .setCrop(true)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.user_head)//加载中默认显示图片
                .setFailureDrawableId(R.drawable.user_head)//加载失败后默认显示图片
                .build();
    }

    private void init() {
//        mMediaPlayer = MediaPlayer.create(this, R.raw.notify);
        mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        initImageLoader(getApplicationContext());
        // 若用户登陆过，则先从好友数据库中取出好友list存入内存中
        if (BmobUserManager.getInstance(getApplicationContext())
                .getCurrentUser() != null) {
            // 获取本地好友user list到内存,方便以后获取好友list
            contactList = CollectionUtils.list2map(BmobDB.create(getApplicationContext()).getContactList());
        }
        initBaidu();
    }

    /**
     * 初始化ImageLoader
     */
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                "bmobim/Cache");// 获取到缓存的目录地址
        // 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                // 线程池内加载的数量
                .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                        // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
                        // .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);// 全局初始化此配置
    }

    /**
     * 退出登录,清空缓存数据
     */
    public void logout() {
        BmobUserManager.getInstance(getApplicationContext()).logout();
        mCurrentUser=null;
        setContactList(null);
        setLatitude(null);
        setLongtitude(null);
    }

    private Map<String, BmobChatUser> contactList = new HashMap<String, BmobChatUser>();

    /**
     * 获取内存中好友user list
     *
     * @return
     */
    public Map<String, BmobChatUser> getContactList() {
        return contactList;
    }

    /**
     * 设置好友user list到内存中
     *
     * @param contactList
     */
    public void setContactList(Map<String, BmobChatUser> contactList) {
        if (this.contactList != null) {
            this.contactList.clear();
        }
        this.contactList = contactList;
    }


    // 单例模式，才能及时返回数据
    SharePreferenceUtil mSpUtil;
    public static final String PREFERENCE_NAME = "_sharedinfo";

    public synchronized SharePreferenceUtil getSpUtil() {
        if (mSpUtil == null) {
            String currentId = BmobUserManager.getInstance(
                    getApplicationContext()).getCurrentUserObjectId();
            String sharedName = currentId + PREFERENCE_NAME;
            mSpUtil = new SharePreferenceUtil(this, sharedName);
        }
        return mSpUtil;
    }

    NotificationManager mNotificationManager;

    public NotificationManager getNotificationManager() {
        if (mNotificationManager == null)
            mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        return mNotificationManager;
    }

    MediaPlayer mMediaPlayer;

    public synchronized MediaPlayer getMediaPlayer() {
//        if (mMediaPlayer == null)
//            mMediaPlayer = MediaPlayer.create(this, R.raw.notify);
        return mMediaPlayer;
    }


    /**
     * 初始化百度相关sdk initBaidumap
     *
     * @param
     * @return void
     * @throws
     * @Title: initBaidumap
     * @Description: TODO
     */
    private void initBaidu() {
        // 初始化地图Sdk
        SDKInitializer.initialize(this);
        // 初始化定位sdk
        initBaiduLocClient();
    }

    /**
     * 初始化百度定位sdk
     *
     * @param
     * @return void
     * @throws
     * @Title: initBaiduLocClient
     * @Description: TODO
     */
    private void initBaiduLocClient() {
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
    }


    public final String PREF_LONGTITUDE = "longtitude";// 经度
    private String longtitude = "";

    /**
     * 获取经度
     *
     * @return
     */
    public String getLongtitude() {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        longtitude = preferences.getString(PREF_LONGTITUDE, "");
        return longtitude;
    }

    /**
     * 设置经度
     *
     * @param pwd
     */
    public void setLongtitude(String lon) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(PREF_LONGTITUDE, lon).commit()) {
            longtitude = lon;
        }
    }

    public final String PREF_LATITUDE = "latitude";// 经度
    private String latitude = "";

    /**
     * 获取纬度
     *
     * @return
     */
    public String getLatitude() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        latitude = preferences.getString(PREF_LATITUDE, "");
        return latitude;
    }

    /**
     * 设置维度
     *
     * @param pwd
     */
    public void setLatitude(String lat) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(PREF_LATITUDE, lat).commit()) {
            latitude = lat;
        }
    }

    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // Receive Location
            double latitude = location.getLatitude();
            double longtitude = location.getLongitude();
            if (lastPoint != null) {
                if (lastPoint.getLatitude() == location.getLatitude()
                        && lastPoint.getLongitude() == location.getLongitude()) {
//					BmobLog.i("两次获取坐标相同");// 若两次请求获取到的地理位置坐标是相同的，则不再定位
                    mLocationClient.stop();
                    return;
                }
            }
            lastPoint = new BmobGeoPoint(longtitude, latitude);
        }

    }
}




//        //初始化Bmob
//        Bmob.initialize(this, "b6df61fc2e46b2ba781525d42e8b318a");
//
//        BmobChat.DEBUG_MODE = true;
//        //BmobIM SDK初始化--只需要这一段代码即可完成初始化
//        BmobChat.getInstance(this).init("b6df61fc2e46b2ba781525d42e8b318a");

//BmobFile缓存目录  缓存总目录下，包含上面两个分目录，分别用于保存下载文件和缩略图
//        BmobConfiguration config = new BmobConfiguration.Builder(this).customExternalCacheDir("myBmobFile").build();
//        BmobPro.getInstance(this).initConfig(config);

