package com.pxy.studyhelper.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.pxy.studyhelper.MyApplication;
import com.pxy.studyhelper.R;
import com.pxy.studyhelper.adapter.QuestionPagerAdapter;
import com.pxy.studyhelper.entity.FavoriteQuestion;
import com.pxy.studyhelper.entity.Question;
import com.pxy.studyhelper.fragment.QuestionFragment;
import com.pxy.studyhelper.utils.CompressImage;
import com.pxy.studyhelper.utils.Tools;

import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


@ContentView(value = R.layout.activity_my_collection)
public class MyCollectionActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    @ViewInject(value = R.id.viewpager)
    private ViewPager mViewPager;
    @ViewInject(value = R.id.iv_share)
    private ImageView  ivShare;
    @ViewInject(value = R.id.iv_add_collection)
    private ImageView  ivAddCollection;

    private QuestionPagerAdapter adapter;
    private List<Question> mQuestionList=new ArrayList<>();
    //当前问题游标
    private int mCurrentIndex=0;
    private int mTotalQSize;
    private Question  mCurrentQuestion;
    private Context  context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        context=this;
        getFavQ();
        mTotalQSize =mQuestionList.size();
        if(mTotalQSize<1)return;
        mCurrentQuestion=mQuestionList.get(0);

        adapter=new QuestionPagerAdapter(getSupportFragmentManager(),mQuestionList,mViewPager,1);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(this);

        ivAddCollection.setImageResource(R.drawable.icon_favor2);
    }

    private void  getFavQ(){
        try {
            FavoriteQuestion f;
             List<FavoriteQuestion> mFavoriteQuestionList= MyApplication.dbManager.selector(FavoriteQuestion.class).findAll();
            if(mFavoriteQuestionList==null||mFavoriteQuestionList.size()<1){
                Tools.ToastShort("您暂时没有收藏题目,赶紧去题库中心做练习吧...");
                MyCollectionActivity.this.finish();
                return;
            }
            for(int i=0;i<mFavoriteQuestionList.size();i++){
                f=mFavoriteQuestionList.get(i);
                Question  q=new Question();
                q.question=f.getQuestion();
                q.answerA=f.getAnswerA();
                q.answerB=f.getAnswerB();
                q.answerC=f.getAnswerC();
                q.answerD=f.getAnswerD();
                q.answerE=f.getAnswerE();
                q.explaination=f.getExplaination();
                q.rightAnswer=f.getRightAnswer();
                q.isWrong=f.getIsWrong();
                q.isFavorite=1;
                mQuestionList.add(q);
            }
        } catch (DbException e) {
            LogUtil.e("get  favQ  error--"+e.getMessage());
        }
    }


    @Event(value ={R.id.iv_preQ,R.id.iv_add_collection,R.id.iv_nextQ,R.id.iv_back,R.id.iv_share,R.id.iv_see_answer},
            type = View.OnClickListener.class)
    private  void  doClick(View view){
        switch (view.getId()){
            case R.id.iv_preQ:
                mViewPager.setCurrentItem(--mCurrentIndex);
                break;
            case R.id.iv_add_collection:
                //todo 加入收藏
                addToFav();
                break;
            case R.id.iv_nextQ:
                mViewPager.setCurrentItem(++mCurrentIndex);
                if(mCurrentIndex== mTotalQSize){
                    //todo  最后一题  交卷操作
                    AlertDialog.Builder  builder=new AlertDialog.Builder(context);
                    builder.setIcon(R.drawable.ic_luncher)
                            .setTitle("提示")
                            .setMessage("当前是最后一题,是否退出我的收藏题库")
                            .setNegativeButton("取消",null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();                                }
                            });
                    builder.show();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_share:
                //todo  showMenu; 包含 三个菜单  一键分享  错题反馈  黑白模式切换
                showMenu();
                break;
            case R.id.iv_see_answer:
                //todo  查看答案
                ((QuestionFragment) adapter.getCurrentFragment()).showAnswer();
                break;
        }
    }

    private void showMenu() {
        AlertDialog.Builder  builder=new AlertDialog.Builder(context);
        builder.setItems(new String[]{"一键分享", "错题反馈", "夜间模式"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:shareQuestion();break;
                }
            }
        });
        builder.show();
    }

    private void shareQuestion() {
        //// TODO: 2016-02-22   截图分享 问题
        Bitmap bp=captureScreen(MyCollectionActivity.this);
        if( !CompressImage.compressFromBpToFile(context, bp)){Tools.ToastShort("压缩图片失败");return;}
        Intent intent=new Intent(context, MakeTopicActivity.class);
        intent.putExtra("boolean", true);
        startActivity(intent);
    }
    public  Bitmap captureScreen(Activity activity) {
        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
        Bitmap bmp=getWindow().getDecorView().getDrawingCache();
        return bmp;
    }

    /**
     * 收藏题目
     */
    private void addToFav() {
        //todo  判断是否已被收藏
        FavoriteQuestion favQ=new FavoriteQuestion();
        if(isFav()) {
            try {
                if(favQs!=null){
                    MyApplication.dbManager.delete(favQs);
                    LogUtil.e("delete  favQ  success");}
                Tools.ToastShort("取消收藏成功");
                ivAddCollection.setImageResource(R.drawable.icon_favor1);
            } catch (DbException e) {
                LogUtil.e(e.getMessage());
            }
            return;
        }
        favQ.setQuestion(mCurrentQuestion.getQuestion());
        favQ.setAnswerA(mCurrentQuestion.getAnswerA());
        favQ.setAnswerB(mCurrentQuestion.getAnswerB());
        favQ.setAnswerC(mCurrentQuestion.getAnswerC());
        favQ.setAnswerD(mCurrentQuestion.getAnswerD());
        favQ.setAnswerE(mCurrentQuestion.getAnswerE());
        favQ.setRightAnswer(mCurrentQuestion.getRightAnswer());
        favQ.setExplaination(mCurrentQuestion.getExplaination());
        favQ.setIsWrong(mCurrentQuestion.isWrong);
        try {
            MyApplication.dbManager.save(favQ);
            ivAddCollection.setImageResource(R.drawable.icon_favor2);
            Tools.ToastShort("收藏成功");
        } catch (DbException e) {
            LogUtil.e(e.getMessage());
        }
    }

    private FavoriteQuestion favQs;
    private boolean  isFav(){
        favQs=null;
        try {
            favQs=MyApplication.dbManager.selector(FavoriteQuestion.class).where("answerA","LIKE",mCurrentQuestion.getAnswerA()).findFirst();
        } catch (DbException e) {
            LogUtil.e(e.getMessage());
        }
        if(favQs!=null) {
            LogUtil.e(favQs.toString());
            return true;
        }
        return false;
    }




    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        LogUtil.e("onPageSelected---"+position);
        mCurrentQuestion=mQuestionList.get(position);
        mCurrentIndex=position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        LogUtil.e("onPageScrollStateChanged---"+state);
        if(isFav()) {
            ivAddCollection.setImageResource(R.drawable.icon_favor2);
        }else {
            ivAddCollection.setImageResource(R.drawable.icon_favor1);
        }

    }
}
