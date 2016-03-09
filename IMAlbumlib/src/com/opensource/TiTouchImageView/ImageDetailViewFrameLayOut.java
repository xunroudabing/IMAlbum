package com.opensource.TiTouchImageView;

import java.util.ArrayList;
import com.opensource.TiTouchImageView.bean.ImagePagerUri;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * BaseImagesViewPagerFrameLayout ==> ImageDetailViewFrameLayOut
 * 滑动预览的基础类 ==> 逻辑类 ==>业务层
 * 
 * @Description:逻辑类
 * @author zhangjianlin
 * @date 2015-8-19 下午4:47:17
 */
public class ImageDetailViewFrameLayOut extends BaseImagesViewPagerFrameLayout {

	private String mPhotoCheck;// 选中的图片uri地址
	private int checkIndex = -1;
	private int pageNum = 0;
	private IImageActionCallback mIImageActionCallback = null;

	public void setIImageActionCallback(IImageActionCallback mIImageActionCallback) {
		this.mIImageActionCallback = mIImageActionCallback;
	}

	public interface IImageActionCallback {
		public void onPageSelected(int index);

		public void onImageLongClick();

		public void onGifLongClick();
		
		public void OnGifOnclick();
		public void OnImageOnclick();
	}

	public void setImages(ArrayList<ImagePagerUri> mImageList, String mPhotoCheck, int checkIndex) {
		this.mImageList = mImageList;
		this.mPhotoCheck = mPhotoCheck;
		this.checkIndex = checkIndex;
		initDatas();
	}

	/** 初始化 数据*/
	private void initDatas() {
		if (checkIndex < 0) {
			for (int i = 0; i < mImageList.size(); i++) {
				ImagePagerUri temp = mImageList.get(i);
				if (!TextUtils.isEmpty(mPhotoCheck) && !TextUtils.isEmpty(temp.getLocalPath())
						&& temp.getLocalPath().equals(mPhotoCheck)) {
					checkIndex = i;
					break;
				} else if (!TextUtils.isEmpty(mPhotoCheck) && !TextUtils.isEmpty(temp.getImgUrl())
						&& temp.getImgUrl().equals(mPhotoCheck)) {
					checkIndex = i;
					break;
				}
			}
		}
		mPagerAdapter.notifyDataSetChanged();
		if (checkIndex != -1) {
			mImageDetailViewPager.setCurrentItem(checkIndex, false);
			if (mIImageActionCallback != null) mIImageActionCallback.onPageSelected(checkIndex);
			// refreshCurrentPageInfo(checkIndex);
		} else {
			mImageDetailViewPager.setCurrentItem(0, false);
			if (mIImageActionCallback != null) mIImageActionCallback.onPageSelected(0);
			// refreshCurrentPageInfo(0);
		}
	}

	public ImageDetailViewFrameLayOut(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ImageDetailViewFrameLayOut(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		pageNum = arg0;
		if (mIImageActionCallback != null) mIImageActionCallback.onPageSelected(arg0);
		Object frag = mPagerAdapter.instantiateItem(mImageDetailViewPager, arg0);
	}

	@Override
	public void onImageLongClick() {
		// TODO Auto-generated method stub
		if (mIImageActionCallback != null) mIImageActionCallback.onImageLongClick();
	}

	@Override
	public void onGifLongClick() {
		// TODO Auto-generated method stub
		if (mIImageActionCallback != null) mIImageActionCallback.onGifLongClick();
	}

	public void delImage() {
		mImageList.remove(pageNum);
		mImageDetailViewPager.setAdapter(mPagerAdapter);
		if (mImageList.size() != 0) pageNum = pageNum % mImageList.size();
		if (mIImageActionCallback != null) mIImageActionCallback.onPageSelected(pageNum);
		if (mImageList.size() != 0) {
			mPagerAdapter.notifyDataSetChanged();
			mImageDetailViewPager.setCurrentItem(pageNum);
		}
	}

	public int getPageSize() {
		return mImageList.size();
	}

	public ArrayList<ImagePagerUri> getImageUriList() {
		return mImageList;
	}

	@Override
	public void onClickImageListener(View v) {
		if (mIImageActionCallback != null) mIImageActionCallback.OnImageOnclick();
	}

	@Override
	public void onGifClick() {
		if (mIImageActionCallback != null) mIImageActionCallback.OnGifOnclick();
	}
}
