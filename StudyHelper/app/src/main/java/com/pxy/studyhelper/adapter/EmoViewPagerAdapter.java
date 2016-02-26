package com.pxy.studyhelper.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**表情布局
  * @ClassName: EmoViewPagerAdapter
  * @Description: TODO
  * @author smile
  * @date 2014-6-17 ����4:17:18
  */
public class EmoViewPagerAdapter extends PagerAdapter{

	private List<View> views;

	public EmoViewPagerAdapter(List<View> views){
		this.views = views;
	}
	
	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(views.get(arg1));
		return views.get(arg1);
	}
	
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(views.get(arg1));
		
	}

}
