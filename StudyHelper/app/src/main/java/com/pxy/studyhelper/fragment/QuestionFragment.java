package com.pxy.studyhelper.fragment;


import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pxy.studyhelper.R;
import com.pxy.studyhelper.entity.Question;
import com.pxy.studyhelper.utils.Tools;

import org.xutils.common.util.LogUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {
    private View rootView;
    private TextView tvQuestion;
    private RadioGroup mRadiogroup;
    private RadioButton[]  mRadioButtons=new RadioButton[5];
    private TextView tvExplaination;

    private ViewPager mViewPager;
    private Question mQuestion;
    private int index;
    private int mode;
    private int total;//问题总数量

    private Handler  handler=new Handler();

    public QuestionFragment() {
        // Required empty public constructor
    }

    public QuestionFragment(ViewPager  viewPager){
        super();
        mViewPager=viewPager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_question, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeAllViewsInLayout();
        }

        mQuestion= (Question) getArguments().getSerializable("question");
        if(mQuestion==null){
            Tools.ToastShort("question  null");
//            getActivity().finish();
        }
        index=getArguments().getInt("index",0);
        mode=getArguments().getInt("mode",0);
        total=getArguments().getInt("total",10);

        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        tvQuestion= (TextView) rootView.findViewById(R.id.question);
        mRadiogroup= (RadioGroup) rootView.findViewById(R.id.radioGroup);
        mRadioButtons[0]= (RadioButton) rootView.findViewById(R.id.answerA);
        mRadioButtons[1]= (RadioButton) rootView.findViewById(R.id.answerB);
        mRadioButtons[2]= (RadioButton) rootView.findViewById(R.id.answerC);
        mRadioButtons[3]= (RadioButton) rootView.findViewById(R.id.answerD);
        mRadioButtons[4]= (RadioButton) rootView.findViewById(R.id.answerE);
        tvExplaination= (TextView) rootView.findViewById(R.id.explaination);

        tvQuestion.setText(index + ". " + mQuestion.getQuestion());

        mRadioButtons[0].setText(mQuestion.getAnswerA());
        mRadioButtons[1].setText(mQuestion.getAnswerB());
        mRadioButtons[2].setText(mQuestion.getAnswerC());
        mRadioButtons[3].setText(mQuestion.getAnswerD());
        if(mQuestion.getAnswerE()!=null&&!mQuestion.getAnswerE().equals("")){
            mRadioButtons[4].setVisibility(View.VISIBLE);
            mRadioButtons[4].setText(mQuestion.getAnswerE());
        }

        mRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(mode==1){//练习模式
                    //todo  显示正确答案效果
                    showAnswer();
                }else {
                    for (int i = 0; i < mRadioButtons.length; i++) {
                        if (mRadioButtons[i].isChecked()) mQuestion.setSelectedAnswer(i);
                    }
                    if (mode == 2 ) {//错题模式 判断正误显示正确答案
                        if(!CheckAnswer()) showAnswer();
                        else {
                            Tools.ToastShort("恭喜你，答对啦...");
                        }// TODO: 2016-02-22   显示答对了
                    } else if (mode == 0) {

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mViewPager.setCurrentItem(index++);
                                LogUtil.i("index------"+index);
                            }
                        },200);
                    }
                }
                if(total==index) {
                    //todo  最后一题  交卷操作
                    if(mode==0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setIcon(R.drawable.ic_luncher)
                                .setTitle("提示")
                                .setPositiveButton("确定", null);
                        builder.setMessage("当前是最后一题，可以交卷了！");
                        builder.show();
                    }
                }
            }
        });

    }

    /**
     * 显示正确答案
     */
    public void showAnswer(){
        tvExplaination.setVisibility(View.VISIBLE);
        tvExplaination.setText(mQuestion.getExplaination());
        if(mQuestion==null) return;
        String str=null;
        switch (mQuestion.getRightAnswer()){
            case 0:str="A";break;
            case 1:str="B";break;
            case 2:str="C";break;
            case 3:str="D";break;
            case 4:str="E";break;
        }
        tvExplaination.setText("正确答案: " + str + "\n" + "解析: " + mQuestion.getExplaination());
    }

    /**
     * 检查答题正误
     * @return
     */
    private boolean CheckAnswer(){
        return  mQuestion.getRightAnswer()==mQuestion.getSelectedAnswer()?true:false;
    }



}
