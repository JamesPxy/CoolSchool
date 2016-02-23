package com.pxy.studyhelper.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.pxy.studyhelper.entity.Question;
import com.pxy.studyhelper.fragment.QuestionFragment;

import java.util.List;

/**
 * User: Pxy(15602269883@163.com)
 * Date: 2016-02-21
 * Time: 22:17
 * FIXME
 */
public class QuestionPagerAdapter  extends FragmentPagerAdapter {
    private ViewPager viewPager;
    private List<Question>  questionList;
    private Fragment mCurrentFragment;
    private int mode;

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    private Question  mCurrentQuestion;
    public QuestionPagerAdapter(FragmentManager  fm,List<Question>  questionList,ViewPager  viewPager,int mode){
        super(fm);
        this.questionList=questionList;
        this.viewPager=viewPager;
        this.mode=mode;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return super.isViewFromObject(view, object);
    }

    @Override
    public Fragment getItem(int position) {
        mCurrentQuestion=questionList.get(position);
        mCurrentFragment=new QuestionFragment(viewPager);
        Bundle bundle=new Bundle();
        bundle.putSerializable("question",mCurrentQuestion);
        bundle.putSerializable("index",position+1);
        bundle.putInt("mode",mode);
        bundle.putInt("total",questionList.size());
        mCurrentFragment.setArguments(bundle);
        return mCurrentFragment;
    }

    @Override
    public int getCount() {
        return questionList.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        mCurrentFragment = (Fragment) object;
    }


}
