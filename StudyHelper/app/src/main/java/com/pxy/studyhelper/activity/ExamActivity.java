package com.pxy.studyhelper.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pxy.studyhelper.MyApplication;
import com.pxy.studyhelper.R;
import com.pxy.studyhelper.adapter.QuestionPagerAdapter;
import com.pxy.studyhelper.biz.TestDao;
import com.pxy.studyhelper.entity.FavoriteQuestion;
import com.pxy.studyhelper.entity.Question;
import com.pxy.studyhelper.fragment.QuestionFragment;
import com.pxy.studyhelper.utils.CompressImage;
import com.pxy.studyhelper.utils.DialogUtil;
import com.pxy.studyhelper.utils.Tools;

import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import cn.bmob.v3.listener.UpdateListener;

@ContentView(value = R.layout.activity_new_exam)
public class ExamActivity extends FragmentActivity implements ViewPager.OnPageChangeListener{

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
    @ViewInject(value = R.id.lv_submit)
    private LinearLayout  lv_submit;
    @ViewInject(value = R.id.lv_see_answer)
    private LinearLayout  lv_seeAnswer;
    @ViewInject(value = R.id.tv_title)
    private TextView  tvTitle;

    private QuestionPagerAdapter  adapter;
    private TestDao  mTestDao;
    private List<Question> mQuestionList;
    //当前问题游标
    private int mCurrentIndex=0;
    private int mTotalQusestion;
    private Question  mCurrentQuestion;
    private boolean  isRuning=true;
    //三种模式  0---测试模式  1---练习模式  2---错题模式
    private int mode;
    //添加笔记及注释
    private String explain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mode=getIntent().getIntExtra("mode",0);
        String name=getIntent().getStringExtra("dbName");
        LogUtil.i("dbName--" + name);
        mTestDao=new TestDao(this,name);
        if(mode==2){
            mQuestionList=mTestDao.getWrongQuestion();
        }else {
            mQuestionList = mTestDao.getQuestions();
        }
        mTotalQusestion=mQuestionList.size();
        if(mTotalQusestion<=0){
            if(mode==2)Tools.ToastShort("暂无该章节对应错题");
            else Tools.ToastShort("获取试题失败...");
            finish();
            return;
        }
        mCurrentQuestion=mQuestionList.get(0);
        setView();
    }

    private void setView() {
//        LogUtil.i(mCurrentQuestion.toString());
        if(mode==0) {//考核模式
            new AlertDialog.Builder(this).setTitle("提示")
                    .setMessage("考核开始,时间为"+mTotalQusestion+"分钟,加油!")
                    .setIcon(R.drawable.ic_luncher)
                    .setPositiveButton("确定",null)
                    .show();
            chronometer.start();
            final String mTime = 01 + ":00";
            LogUtil.e(mTime);
            chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
//                重置时间  chronometer.setBase(SystemClock.elapsedRealtime());
                    if (mTime.equals(chronometer.getText())) {
                        new AlertDialog.Builder(ExamActivity.this).setIcon(R.drawable.ic_luncher)
                                .setTitle("提示")
                                .setMessage("交卷时间到了,还没做完么,下次要抓紧时间了,加油!")
                                .setNegativeButton("取消", null)
                                .setPositiveButton("确定", null)
                                .show();
                    }
                }
            });
        }else {
            ivTime.setVisibility(View.GONE);
            chronometer.setVisibility(View.GONE);
        }

        adapter=new QuestionPagerAdapter(getSupportFragmentManager(),mQuestionList,mViewPager,mode);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(this);
        if(isFav()) {
            ivAddCollection.setImageResource(R.drawable.icon_favor2);
        }else {
            ivAddCollection.setImageResource(R.drawable.icon_favor1);
        }

        switch (mode){
            case 0:
                lv_submit.setVisibility(View.VISIBLE);
                lv_seeAnswer.setVisibility(View.GONE);
                break;
            case 1:case 2:
                lv_seeAnswer.setVisibility(View.VISIBLE);
                lv_submit.setVisibility(View.GONE);
                tvTitle.setVisibility(View.VISIBLE);
                if(mode==2)tvTitle.setText("错题重做");
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTestDao.getDb()!=null&&mTestDao.getDb().isOpen()){
            mTestDao.getDb().close();
            mTestDao=null;
        }
    }


    @Override
    public void onBackPressed() {
        doBackBiz();
    }

    private void doBackBiz() {
        if(mode==0&&mCurrentIndex<mQuestionList.size()-1) {
            new AlertDialog.Builder(this).setTitle("提醒")
                    .setIcon(R.drawable.ic_luncher)
                    .setMessage("试题还没有答完，是否要现在退出")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i = 0; i <=mCurrentIndex; i++) {
                                mCurrentQuestion = mQuestionList.get(i);
                                if (mCurrentQuestion.isWrong==0&&mCurrentQuestion.getRightAnswer()!= mCurrentQuestion.getSelectedAnswer()) {
                                    mTestDao.updateQuestion(mCurrentQuestion.getAnswerA(),1);
                                    LogUtil.i("考核模式  save6666666666");
                                }
                            }
                            finish();
                        }
                    })
                    .show();
        }else if(mode==2){
            new AlertDialog.Builder(this).setTitle("提醒")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage("是否要现在退出")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i = 0; i<mCurrentIndex; i++) {
                                mCurrentQuestion = mQuestionList.get(i);
                                if (mCurrentQuestion.isWrong==1&mCurrentQuestion.getRightAnswer()== mCurrentQuestion.getSelectedAnswer()) {
                                    mTestDao.updateQuestion(mCurrentQuestion.getAnswerA(),0);
                                    LogUtil.i("错题模式 save6666666666");
                                }
                            }
                            finish();
                        }
                    }).show();

        }else {
            finish();
        }
    }

    @Event(value ={R.id.iv_preQ,R.id.lv_submit,R.id.lv_add_collection,R.id.iv_nextQ,R.id.iv_back,R.id.iv_share,R.id.iv_time,R.id.lv_see_answer,R.id.lv_add_note},
            type = View.OnClickListener.class)
    private  void  doClick(View view){
        switch (view.getId()){
            case R.id.iv_preQ:
                if(mCurrentIndex>0)
                    mViewPager.setCurrentItem(--mCurrentIndex);
                else Tools.ToastShort("当前是第一题");
                break;
            case R.id.lv_submit:
                //交卷操作
                DialogUtil.showResultDialog(ExamActivity.this, getScore());
                break;
            case R.id.lv_add_note:
                // TODO: 2016-03-18 添加笔记
                addNote();
                break;
            case R.id.lv_add_collection:
                //todo 加入收藏
                addToFav();
                break;
            case R.id.iv_nextQ:
                if(mCurrentIndex==mTotalQusestion){
                    //todo  最后一题  交卷操作
                    AlertDialog.Builder  builder=new AlertDialog.Builder(ExamActivity.this);
                    if(mode==0){
                        builder.setMessage("当前是最后一题，可以交卷了！");
                        builder.setIcon(R.drawable.ic_luncher)
                                .setTitle("提示")
                                .setNegativeButton("取消",null)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DialogUtil.showResultDialog(ExamActivity.this, getScore());
                                    }
                                });
                    }
                    else{
                        builder.setMessage("当前是最后一题,是否退出!");
                        builder.setIcon(R.drawable.ic_luncher)
                                .setTitle("提示")
                                .setNegativeButton("取消",null)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(mode==2) {
                                            for (int i = 0; i < mCurrentIndex; i++) {
                                                mCurrentQuestion = mQuestionList.get(i);
                                                if (mCurrentQuestion.isWrong == 1 & mCurrentQuestion.getRightAnswer() == mCurrentQuestion.getSelectedAnswer()) {
                                                    mTestDao.updateQuestion(mCurrentQuestion.getAnswerA(), 0);
                                                }
                                            }
                                        }
                                        finish();
                                    }
                                });
                    }

                    builder.show();
                }else{
                    mViewPager.setCurrentItem(++mCurrentIndex);
                }
                break;
            case R.id.iv_back:
                doBackBiz();
                break;
            case R.id.iv_share:
                //todo  showMenu; 包含 三个菜单  一键分享  错题反馈  黑白模式切换
                showMenu();
                break;
            case R.id.iv_time:
                PauseTime();
                break;
            case R.id.lv_see_answer:
                //todo  查看答案
                ((QuestionFragment) adapter.getCurrentFragment()).showAnswer();
                break;
        }
    }

    private void addNote() {
        AlertDialog.Builder  builder=new AlertDialog.Builder(ExamActivity.this);
        View view=View.inflate(ExamActivity.this, R.layout.update_info_layout, null);
        final EditText edt= (EditText) view.findViewById(R.id.editText);
        edt.setHint("请输入笔记");
        builder.setTitle("添加笔记");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setView(view);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                explain = edt.getText().toString().trim();
                if (TextUtils.isEmpty(explain)) {
                    Tools.ToastShort("所添加笔记不能为空...");
                    return;
                }
                if (!mCurrentQuestion.getExplaination().equals("暂无"))
                    explain = mCurrentQuestion.getExplaination() + "\n" + explain;
                boolean isAddSuccess = mTestDao.addNote(mCurrentQuestion.getId(), explain);
                if (isAddSuccess) {
                    Tools.ToastShort("添加笔记成功");
                    mCurrentQuestion.setExplaination(explain);
                }
            }
        });
        builder.show();


    }

    private void showMenu() {
        AlertDialog.Builder  builder=new AlertDialog.Builder(ExamActivity.this);
//                builder.setIcon(R.drawable.ic_luncher);
//                builder.setTitle("选择模式:");
        builder.setItems(new String[]{"一键分享", "错题反馈"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:shareQuestion();break;
                    case 1:startActivity(new Intent(ExamActivity.this,ActivityFeedBack.class));break;
                }
            }
        });
        builder.show();
    }

    private void shareQuestion() {
        //// TODO: 2016-02-22   截图分享 问题
        Bitmap bp=captureScreen(ExamActivity.this);
        ivTime.setImageBitmap(bp);
        if( !CompressImage.compressFromBpToFile(ExamActivity.this, bp)){Tools.ToastShort("压缩图片失败");return;}
        Intent  intent=new Intent(ExamActivity.this, MakeTopicActivity.class);
        intent.putExtra("boolean",true);
        startActivity(intent);
    }
    public  Bitmap captureScreen(Activity activity) {
        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
        Bitmap bmp=getWindow().getDecorView().getDrawingCache();
        return bmp;
    }
    private void PauseTime() {
        if(isRuning){
            chronometer.stop();
            Tools.ToastShort("先休息一下,让脑子飞一会吧...");
            ivTime.setImageResource(R.drawable.btn_quiz_stop_dark);
            isRuning=false;
        }else{
            chronometer.start();
            Tools.ToastShort("继续开始奋战...");
            ivTime.setImageResource(R.drawable.btn_quiz_begin);
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

    private int getScore(){
        int  right=0,score;
        for(int i=0;i<mTotalQusestion;i++) {
            mCurrentQuestion=mQuestionList.get(i);
            if (mCurrentQuestion.getRightAnswer() == mCurrentQuestion.getSelectedAnswer()) {
                if(mCurrentQuestion.isWrong!=0) mTestDao.updateQuestion(mCurrentQuestion.getAnswerA(),0);
                right++;
            } else {//答错
                if(mCurrentQuestion.isWrong!=1) mTestDao.updateQuestion(mCurrentQuestion.getAnswerA(),1);
            }
        }
        score=(int)((right*1.0/mTotalQusestion)*100);

        LogUtil.e("get score----------" + score);
        //保存分数至用户个人信息数据库
        int currentScore=MyApplication.mCurrentUser.getScore();
        if(10<currentScore&&currentScore<100){
            MyApplication.mCurrentUser.setLevel(1);
        }
        else if(100<currentScore&&currentScore<200){
            MyApplication.mCurrentUser.setLevel(2);
        }
        else if(200<currentScore&&currentScore<300){
            MyApplication.mCurrentUser.setLevel(3);
        }
        else if(300<currentScore&&currentScore<400){
            MyApplication.mCurrentUser.setLevel(4);
        }
        else if(400<currentScore&&currentScore<500){
            MyApplication.mCurrentUser.setLevel(5);
        }else{
            MyApplication.mCurrentUser.setLevel(6);
        }
        MyApplication.mCurrentUser.setScore(currentScore+score);
        MyApplication.mCurrentUser.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                LogUtil.i("save score success");
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtil.i("save score failure" + s);
            }
        });
        return  score;
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
