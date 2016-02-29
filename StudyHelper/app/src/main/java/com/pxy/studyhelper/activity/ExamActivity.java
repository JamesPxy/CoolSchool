package com.pxy.studyhelper.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pxy.studyhelper.MyApplication;
import com.pxy.studyhelper.R;
import com.pxy.studyhelper.biz.TestDao;
import com.pxy.studyhelper.entity.FavoriteQuestion;
import com.pxy.studyhelper.entity.Question;
import com.pxy.studyhelper.utils.DialogUtil;
import com.pxy.studyhelper.utils.Tools;

import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 试题测试Activity
 */
@ContentView(value = R.layout.activity_exam)
public class ExamActivity extends AppCompatActivity {
    @ViewInject(value = R.id.question)
    private TextView   tvQuestion;
    @ViewInject(value = R.id.radioGroup)
    private RadioGroup mRadioGroup;
    @ViewInject(value = R.id.answerA)
    private RadioButton mAnswerA;
    @ViewInject(value = R.id.answerB)
    private RadioButton   mAnswerB;
    @ViewInject(value = R.id.answerC)
    private RadioButton   mAnswerC;
    @ViewInject(value = R.id.answerD)
    private RadioButton   mAnswerD;
    @ViewInject(value = R.id.answerE)
    private RadioButton   mAnswerE;
    @ViewInject(value = R.id.explaination)
    private TextView  tvExplaination;
    @ViewInject(value = R.id.iv_preQ)
    private ImageView  ivPreQ;
    @ViewInject(R.id.iv_add_note)
    private ImageView  ivAddNote;
    @ViewInject(value = R.id.iv_add_collection)
    private ImageView  ivAddCollection;
    @ViewInject(value = R.id.iv_nextQ)
    private ImageView  ivNextQ;

    @ViewInject(value = R.id.chronometer)
    private Chronometer  chronometer;
    @ViewInject(value = R.id.iv_time)
    private ImageView  ivTime;

    private TestDao  mTestDao;
    private List<Question>  mQuestionList;
    //当前问题游标
    private int mCurrentIndex=0;
    private int mTotalQusestion;
    private Question  mCurrentQuestion;
    private String  mTime;
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
            showQuestion(mCurrentIndex);
        }else{
            Tools.ToastShort("获取试题失败...");
            this.finish();
            return;
        }
        initView();
    }

    private void initView() {

        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("考试开始,考试时间为"+mTotalQusestion+"分钟,加油!")
                .setIcon(R.drawable.ic_luncher)
                .setPositiveButton("确定",null)
                .show();

        chronometer.start();
        mTime=mTotalQusestion+":00";
        LogUtil.e(mTime);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
//                重置时间  chronometer.setBase(SystemClock.elapsedRealtime());
                if(mTime.equals(chronometer.getText())){
                    new AlertDialog.Builder(ExamActivity.this).setIcon(R.drawable.ic_luncher)
                            .setTitle("提示")
                            .setMessage("交卷时间到了,还没做完吗?下次要抓紧时间了,加油!")
                            .setNegativeButton("取消",null)
                            .setPositiveButton("确定",null)
                            .show();
                }
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checked = -1;
                switch (checkedId) {
                    case R.id.answerA:
                        checked = 0;
                        break;
                    case R.id.answerB:
                        checked = 1;
                        break;
                    case R.id.answerC:
                        checked = 2;
                        break;
                    case R.id.answerD:
                        checked = 3;
                        break;
                    case R.id.answerE:
                        checked = 4;
                        break;
                }
                //标记选中答案
                mCurrentQuestion.setSelectedAnswer(checked);
            }
        });
    }

    /**
     * 显示问题
     * @param index
     */
    private void showQuestion(int index) {
        tvExplaination.setVisibility(View.GONE);
        if(index<0){
            Tools.ToastShort("当前是第一题...");
            mCurrentIndex=0;
        }else if(index<mTotalQusestion){
            mCurrentQuestion=mQuestionList.get(index);
            tvQuestion.setText(index + 1 + ". " + mCurrentQuestion.getQuestion());
            mAnswerA.setText(mCurrentQuestion.getAnswerA());
            mAnswerB.setText(mCurrentQuestion.getAnswerB());
            mAnswerC.setText(mCurrentQuestion.getAnswerC());
            mAnswerD.setText(mCurrentQuestion.getAnswerD());
            if(mCurrentQuestion.getAnswerE().length()>0){
                mAnswerE.setVisibility(View.VISIBLE);
                mAnswerE.setText(mCurrentQuestion.getAnswerE());
            }else{
                mAnswerE.setVisibility(View.GONE);
            }
            //  记住用户选中答案 并显示
            switch (mCurrentQuestion.getSelectedAnswer()){
                case 0:mAnswerA.setChecked(true);break;
                case 1:mAnswerB.setChecked(true); break;
                case 2:mAnswerC.setChecked(true); break;
                case 3:mAnswerD.setChecked(true); break;
                case 4:mAnswerE.setChecked(true); break;
                default: mRadioGroup.clearCheck();break;
            }
            if(isFav()) {
                ivAddCollection.setImageResource(R.drawable.icon_favor2);
            }else {
                ivAddCollection.setImageResource(R.drawable.icon_favor1);
            }
        }else {
            mCurrentIndex=mTotalQusestion-1;
            new AlertDialog.Builder(ExamActivity.this)
                    .setIcon(R.drawable.ic_luncher)
                    .setMessage("已经是最后一题,是否交卷")
                    .setTitle("提示")
                    .setCancelable(false)
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //todo  交卷操作
                            DialogUtil.showResultDialog(ExamActivity.this, getScore());
                        }
                    })
                    .show();
        }
    }
    @Event(value ={R.id.iv_preQ,R.id.iv_add_note,R.id.iv_add_collection,R.id.iv_nextQ,R.id.iv_back,R.id.iv_menu,R.id.iv_time},
            type = View.OnClickListener.class)
    private  void  doClick(View view){
        switch (view.getId()){
            case R.id.iv_preQ:
                showQuestion(--mCurrentIndex);
                break;
            case R.id.iv_add_note:
                //交卷操作
                DialogUtil.showResultDialog(ExamActivity.this, getScore());
                break;
            case R.id.iv_add_collection:
                //todo 加入收藏
                addToFav();
                break;
            case R.id.iv_nextQ:
                showQuestion(++mCurrentIndex);
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
                if(favQs!=null){MyApplication.dbManager.delete(favQs);LogUtil.e("favq  null");}
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
}
