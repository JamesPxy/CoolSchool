package com.pxy.studyhelper.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pxy.studyhelper.R;
import com.pxy.studyhelper.entity.Topic;
import com.pxy.studyhelper.photozoom.PhotoView;

import org.xutils.image.ImageOptions;
import org.xutils.x;


public class ShowTopicDetail extends AppCompatActivity {

    private PhotoView  mPhotoView;
    private Topic mTopic;
    private ImageOptions  imageOptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_topic_detail);
        mTopic= (Topic) getIntent().getSerializableExtra("topic");

        initView();

        imageOptions = new ImageOptions.Builder()
//                .setSize(DensityUtil.dip2px(320), DensityUtil.dip2px(320))//图片大小
//                .setRadius(DensityUtil.dip2px(5))//ImageView圆角半径
//                .setCrop(true)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
//                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.image_grid_default)//加载中默认显示图片
                .setFailureDrawableId(R.drawable.image_grid_default)//加载失败后默认显示图片
                .build();


    }

    private void initView() {
        RelativeLayout rv= (RelativeLayout) findViewById(R.id.include_tabbar);
        rv.setBackgroundColor(Color.BLUE);
        ImageView iv_back= (ImageView) rv.findViewById(R.id.iv_back);
        TextView title=(TextView)rv.findViewById(R.id.tv_title);
        title.setText("图片详情");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mPhotoView= (PhotoView) findViewById(R.id.photoview);
        if(mTopic!=null)
            x.image().bind(mPhotoView,mTopic.getImage().getFileUrl(this),imageOptions);
    }
}
