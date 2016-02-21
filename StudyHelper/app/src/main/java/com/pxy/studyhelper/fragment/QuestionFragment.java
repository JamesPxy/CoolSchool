package com.pxy.studyhelper.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pxy.studyhelper.R;
import com.pxy.studyhelper.entity.Question;
import com.pxy.studyhelper.utils.Tools;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {
    private View rootView;
    private TextView tvQuestion;
    private RadioGroup mRadiogroup;
    private RadioButton[]  mRadioButtons=new RadioButton[5];
    private TextView tvExplaination;

    private Question mQuestion;
    private int index;

    public QuestionFragment() {
        // Required empty public constructor
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
                for(int i=0;i<mRadioButtons.length;i++){
                    if(mRadioButtons[i].isChecked())mQuestion.setSelectedAnswer(i);
                }
            }
        });

    }


}
