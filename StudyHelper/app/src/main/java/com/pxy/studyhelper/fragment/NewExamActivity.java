package com.pxy.studyhelper.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;

import com.pxy.studyhelper.MyApplication;
import com.pxy.studyhelper.R;
import com.pxy.studyhelper.adapter.QuestionPagerAdapter;
import com.pxy.studyhelper.dao.TestDao;
import com.pxy.studyhelper.entity.FavoriteQuestion;
import com.pxy.studyhelper.entity.Question;
import com.pxy.studyhelper.utils.Tools;

import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

@ContentView(value = R.layout.activity_new_exam)
public class NewExamActivity extends FragmentActivity implements ViewPager.OnPageChangeListener{

    @ViewInject(value = R.id.viewpager)
    private ViewPager  mViewPager;
    @ViewInject(value = R.id.iv_time)
    private ImageView  ivTime;
    @ViewInject(value = R.id.iv_share)
    private ImageView  ivShare;
    @ViewInject(value = R.id.chronometer)
    private Chronometer chronometer;
    @ViewInject(value = R.id.iv_add_collection)
    private ImageView  ivAddCollection;

    private QuestionPagerAdapter  adapter;
    private TestDao  mTestDao;
    private List<Question> mQuestionList;
    //当前问题游标
    private int mCurrentIndex=0;
    private int mTotalQusestion;
    private Question  mCurrentQuestion;
    private boolean  isRuning=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        String name=getIntent().getStringExtra("dbName");
        LogUtil.i("dbName--" + name);
        mTestDao=new TestDao(this,name);

        mQuestionList=mTestDao.getQuestions();

        mTotalQusestion=mQuestionList.size();

        if(mTotalQusestion>0){
//            showQuestion(mCurrentIndex);
        }else{
            Tools.ToastShort("获取试题失败...");
            this.finish();
            return;
        }
        mCurrentQuestion=mQuestionList.get(0);
        setView();
    }

    private void setView() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("考试开始,考试时间为"+mTotalQusestion+"分钟,加油!")
                .setIcon(R.drawable.ic_luncher)
                .setPositiveButton("确定",null)
                .show();

        chronometer.start();
      final String  mTime=10+":00";
        LogUtil.e(mTime);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
//                重置时间  chronometer.setBase(SystemClock.elapsedRealtime());
                if (mTime.equals(chronometer.getText())) {
                    new AlertDialog.Builder(NewExamActivity.this).setIcon(R.drawable.ic_luncher)
                            .setTitle("提示")
                            .setMessage("交卷时间到了,还没做完吗?下次要抓紧时间了,加油!")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", null)
                            .show();
                }
            }
        });

        adapter=new QuestionPagerAdapter(getSupportFragmentManager(),mQuestionList,mViewPager);
        mViewPager.setAdapter(adapter);

        if(isFav()) {
            ivAddCollection.setImageResource(R.drawable.icon_favor2);
        }else {
            ivAddCollection.setImageResource(R.drawable.icon_favor1);
        }
    }


    @Event(value ={R.id.iv_preQ,R.id.iv_add_note,R.id.iv_add_collection,R.id.iv_nextQ,R.id.iv_back,R.id.iv_menu,R.id.iv_time},
            type = View.OnClickListener.class)
    private  void  doClick(View view){
        switch (view.getId()){
            case R.id.iv_preQ:
                mCurrentQuestion=mQuestionList.get(--mCurrentIndex);
                mViewPager.setCurrentItem(mCurrentIndex);
                break;
            case R.id.iv_add_note:
                //交卷操作
//                DialogUtil.showResultDialog(NewExamActivity.this, getScore());
                break;
            case R.id.iv_add_collection:
                //todo 加入收藏
                addToFav();
                break;
            case R.id.iv_nextQ:
                mCurrentQuestion=mQuestionList.get(++mCurrentIndex);
                mViewPager.setCurrentItem(mCurrentIndex);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_menu:
                //todo  showMenu;
                break;
            case R.id.iv_time:
                PauseTime();
                break;
        }
    }


    private void PauseTime() {
        if(isRuning){
            chronometer.stop();
            Tools.ToastShort("先休息一下,让脑子飞一会吧...");
            ivTime.setImageResource(android.R.drawable.ic_media_play);
            isRuning=false;
        }else{
            chronometer.start();
            Tools.ToastShort("继续开始奋战...");
            ivTime.setImageResource(android.R.drawable.ic_media_pause);
            isRuning=true;
        }
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
                    MyApplication.dbManager.delete(favQs);LogUtil.e("favq  null");}
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

    FavoriteQuestion favQs=new FavoriteQuestion();
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

    private int getScore(){
        int  right=0,score;
        for(int i=0;i<mTotalQusestion;i++) {
            mCurrentQuestion=mQuestionList.get(i);
            if (mCurrentQuestion.getRightAnswer() == mCurrentQuestion.getSelectedAnswer()) {
                mTestDao.updateQuestion(mCurrentQuestion.getAnswerA(), 0);
                right++;
            } else {//答错
                mTestDao.updateQuestion(mCurrentQuestion.getAnswerA(), 1);
            }
        }
        score=(int)((right*1.0/mTotalQusestion)*100);
        LogUtil.e("getscore----------"+score);
        return  score;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
       mCurrentQuestion=mQuestionList.get(position);
        mCurrentIndex=position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(isFav()) {
            ivAddCollection.setImageResource(R.drawable.icon_favor2);
        }else {
            ivAddCollection.setImageResource(R.drawable.icon_favor1);
        }

    }
}
