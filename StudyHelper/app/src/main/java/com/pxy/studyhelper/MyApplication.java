package com.pxy.studyhelper;

import android.app.Application;
import android.content.Intent;
import android.widget.ImageView;

import com.bmob.BmobConfiguration;
import com.bmob.BmobPro;
import com.pxy.studyhelper.activity.LoginActivity;
import com.pxy.studyhelper.entity.User;

import org.xutils.DbManager;
import org.xutils.common.util.DensityUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * User: Pxy(15602269883@163.com)
 * Date: 2016-01-24
 * Time: 21:16
 * FIXME
 */
public class MyApplication  extends Application {

    public  static User  mCurrentUser;

    public static boolean  isDebug=true;

    public static DbManager  dbManager;

    public static DbManager.DaoConfig  daoConfig;

    public static ImageOptions  imageOptions;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化xUtil
        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志

        //初始化Bmob
        Bmob.initialize(this,"b6df61fc2e46b2ba781525d42e8b318a");

        //BmobFile缓存目录  缓存总目录下，包含上面两个分目录，分别用于保存下载文件和缩略图
        BmobConfiguration config = new BmobConfiguration.Builder(this).customExternalCacheDir("myBmobFile").build();
        BmobPro.getInstance(this).initConfig(config);

////        通过如下方法可获取保存下载文件的完整路径：
////可自行打印此路径，通过文件管理器查看文件是否下载成功
//        BmobPro.getInstance(this).getCacheDownloadDir();
////获取缓存大小  文件大小（单位：字节）
//        String cacheSize = String.valueOf(BmobPro.getInstance(this).getCacheFileSize());
////对文件大小进行格式化，转化为'B'、'K'、'M'、'G'等单位
//        String formatSize = BmobPro.getInstance(this).getCacheFormatSize();
////        清除缓存：
//        BmobPro.getInstance(this).clearCache();


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
                        // ...
                    }
                });//数据库更新操作
        dbManager= x.getDb(daoConfig);

        mCurrentUser= BmobUser.getCurrentUser(this, User.class);
        if(mCurrentUser!=null){
            //缓存对象不为空时允许用户使用
            LogUtil.i(mCurrentUser.toString());
        }else{
            Intent  intent=new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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

}
