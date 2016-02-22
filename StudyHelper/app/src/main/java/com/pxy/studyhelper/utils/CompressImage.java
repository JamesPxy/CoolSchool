package com.pxy.studyhelper.utils;

import android.content.Context;
import android.graphics.Bitmap;

import org.xutils.common.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ljy on 2016-02-22.
 */
public class CompressImage {

    public static boolean compressFromBpToFile(Context  context,Bitmap image) {
//        //定义一个file，为压缩后的图片   File f = new File("图片保存路径","图片名称");
        String path=context.getCacheDir().getAbsolutePath()+"/topicImages.jpg";
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            LogUtil.e(e.getMessage());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//这里100表示不压缩，将不压缩的数据存放到baos中
        int per = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, per, baos);// 将图片压缩为原来的(100-per)%，把压缩后的数据存放到baos中
            per -= 10;// 每次都减少10
        }

        //将输出流写入到新文件
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            baos.close();
            //标记压缩图片成功
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
            return false;
        }finally {
//            //回收图片，清理内存
//            if (image != null && !image.isRecycled()) {
//                image.recycle();
//                image = null;
//                System.gc();
//            }
        }
        return true;
    }
}