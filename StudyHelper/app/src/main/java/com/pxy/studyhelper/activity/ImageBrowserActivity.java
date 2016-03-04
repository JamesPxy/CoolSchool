package com.pxy.studyhelper.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.pxy.studyhelper.R;
import com.pxy.studyhelper.utils.ImageLoadOptions;
import com.pxy.studyhelper.view.CustomViewPager;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**图片浏览
 * @ClassName: ImageBrowserActivity
 * @Description: TODO
 * @author smile
 * @date 2014-6-19 下午8:22:49
 */
public class ImageBrowserActivity extends ActivityBase implements OnPageChangeListener{

	private CustomViewPager mSvpPager;
	private ImageBrowserAdapter mAdapter;
	private int mPosition;

	private ArrayList<String> mPhotos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showpicture);
		init();
		initViews();
	}

	private void init() {
		mPhotos = getIntent().getStringArrayListExtra("photos");
		mPosition = getIntent().getIntExtra("position", 0);
	}

	protected void initViews() {
		mSvpPager = (CustomViewPager) findViewById(R.id.pagerview);
		mAdapter = new ImageBrowserAdapter(this);
		mSvpPager.setAdapter(mAdapter);
		mSvpPager.setCurrentItem(mPosition, false);
		mSvpPager.setOnPageChangeListener(this);

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		mPosition = arg0;
	}

	private class ImageBrowserAdapter extends PagerAdapter{

		private LayoutInflater inflater;

		public ImageBrowserAdapter (Context context){
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mPhotos.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {

			View imageLayout = inflater.inflate(R.layout.item_show_picture,
					container, false);
			final PhotoView photoView = (PhotoView) imageLayout
					.findViewById(R.id.photoview);
			final ProgressBar progress = (ProgressBar)imageLayout.findViewById(R.id.progress);

			final String imgUrl = mPhotos.get(position);
//			x.image().bind(photoView,imgUrl, new Callback.CommonCallback() {
//				@Override
//				public void onSuccess(Object o) {
//					progress.setVisibility(View.GONE);
//				}
//
//				@Override
//				public void onError(Throwable throwable, boolean b) {
//					progress.setVisibility(View.GONE);
//				}
//
//				@Override
//				public void onCancelled(CancelledException e) {
//					progress.setVisibility(View.GONE);
//				}
//
//				@Override
//				public void onFinished() {
//					progress.setVisibility(View.GONE);
//				}
//			});

			ImageLoader.getInstance().displayImage(imgUrl, photoView, ImageLoadOptions.getOptions(),new SimpleImageLoadingListener() {

				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
					progress.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
											FailReason failReason) {
					// TODO Auto-generated method stub
					progress.setVisibility(View.GONE);

				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					// TODO Auto-generated method stub
					progress.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					progress.setVisibility(View.GONE);

				}
			});

			container.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

}
