package com.opensource.TiTouchImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import pl.droidsonroids.gif.GifImageView;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.imalbumlib.R;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.opensource.JazzyViewPager.JazzyViewPager;
import com.opensource.JazzyViewPager.JazzyViewPager.TransitionEffect;
import com.opensource.TiTouchImageView.BaseImageZoomFrameLayout.OnImageZoomLongClick;
import com.opensource.TiTouchImageView.bean.ImagePagerUri;
import com.opensource.configure.Configure;
import com.opensource.configure.imgload.GlobalImageLoader;
import com.opensource.giflib.GifLoader;

/**
 * BaseImagesViewPagerFrameLayout ==> ImageDetailViewFrameLayOut
 * 滑动预览的基础类 ==> 逻辑类 ==>业务层
 * 
 * @description：滑动预览的基础类
 * @author zhangjianlin (990996641)
 * @date 2015年5月5日 上午11:07:41
 */
public abstract class BaseImagesViewPagerFrameLayout extends FrameLayout implements OnPageChangeListener , OnImageZoomLongClick{

	protected JazzyViewPager mImageDetailViewPager;
	protected ArrayList<ImagePagerUri> mImageList = new ArrayList<ImagePagerUri>();
	protected LayoutInflater inflater;
	protected ImagePagerAdapter mPagerAdapter;
	protected boolean isBitmapNull = true;// 图片是否加载成功

	public boolean getIsBitmapNull() {
		return isBitmapNull;
	}

	/**
	 * 逻辑层
	 */
	/** 用于判断对应页面图片是否加载完成，以便没加载完成不允许发送 */
	private GifLoader gifLoader;

	public BaseImagesViewPagerFrameLayout(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		this(context, attrs, 0);
	}

	public BaseImagesViewPagerFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
		// mContext = context;
	}

	public void init(Context context) {
		inflater = LayoutInflater.from(context);
		gifLoader = GifLoader.getInstance(context);
		mImageDetailViewPager = (JazzyViewPager) LayoutInflater.from(context).inflate(R.layout.baseviewpager, null);
		mImageDetailViewPager.setTransitionEffect(TransitionEffect.Standard);
		// mImageDetailViewPager.setFadeEnabled(true);
		mImageDetailViewPager.setOffscreenPageLimit(5);
		mImageDetailViewPager.setOnPageChangeListener(this);
		mPagerAdapter = new ImagePagerAdapter();
		mImageDetailViewPager.setAdapter(mPagerAdapter);
		addView(mImageDetailViewPager);
	}

	public abstract void onImageLongClick();

	public abstract void onGifLongClick();
	public abstract void onGifClick();

	class ImagePagerAdapter extends PagerAdapter {

		private OnLongClickListener mOnLongClickListener = new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				int id = v.getId();
				if (id == R.id.gif_image) {
					onGifLongClick();
				}
				else {
				}
				return false;
			}
		};

		private OnClickListener mClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int id = v.getId();
				if (id == R.id.gif_image) {
					onGifClick();
				}
				else {
				}
			}
			
		};
		
		@Override
		public Object instantiateItem(ViewGroup view, final int position) {
			View imageLayout = inflater.inflate(R.layout.item_gallery_image, view, false);
			assert imageLayout != null;
			GifImageView gifView = (GifImageView) imageLayout.findViewById(R.id.gif_image);
			final BaseImageZoomFrameLayout imageView = (BaseImageZoomFrameLayout) imageLayout
					.findViewById(R.id.shop_gallery_image);
			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
//			mViewArray.set(position, imageView);
			ImagePagerUri photo = mImageList.get(position);
			if (photo != null) {
				String realPath = "";
				if (!TextUtils.isEmpty(photo.getLocalPath()) && new File(photo.getLocalPath()).exists()) {
					realPath = "file://" + photo.getLocalPath();
				}
				else {
					realPath = photo.getImgUrl();
				}
				final Bitmap bitmap = GlobalImageLoader.getIntance().loadImageSync(photo.getSmallImgUrl());
				imageView.setImageBitmap(bitmap);
				isBitmapNull = (bitmap == null);
				// 如果是gif图片的话则用GifImageView来显示
				if (photo.getImgUrl().endsWith(".gif") || photo.getImgUrl().endsWith(".GIF")
						|| realPath.endsWith(".gif") || realPath.endsWith(".GIF")) {
					if (!TextUtils.isEmpty(photo.getLocalPath())) {
						gifLoader.displayImage(photo.getLocalPath(), gifView, true);
					}
					else {
						gifLoader.displayImage(photo.getImgUrl(), gifView, true);
					}
					imageView.setVisibility(View.GONE);
					gifView.setVisibility(View.VISIBLE);
				}
				GlobalImageLoader.getIntance().dispalyImg(imageView.base_imageView, realPath,
						new SimpleImageLoadingListener() {
							@Override
							public void onLoadingStarted(String imageUri, View view) {
								spinner.setVisibility(View.VISIBLE);
								// IMDebug.print("------ImageDetailAct--" +
								// "onLoadingStarted");
							}

							@SuppressWarnings("unused")
							public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
								spinner.setVisibility(View.GONE);
								if (!isBitmapNull) {
									imageView.setImageBitmap(bitmap);
								}
								// IMDebug.print("------ImageDetailAct--" +
								// "onLoadingFailed");

							}

							@Override
							public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
								isBitmapNull = (loadedImage == null);
								spinner.setVisibility(View.GONE);
							}
						}, Configure.getInstance().options_image);
			}

			imageView.setOnImageZoomLongClick(BaseImagesViewPagerFrameLayout.this);
			gifView.setOnLongClickListener(mOnLongClickListener);
			gifView.setOnClickListener(mClickListener);
			view.addView(imageLayout, 0);
			mImageDetailViewPager.setObjectForPosition(imageLayout, position);
			return imageLayout;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mImageList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;// 官方提示这样写 ;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	};

	
	@Override
	public void longClickListener(View v) {
		onImageLongClick();
	}
}
