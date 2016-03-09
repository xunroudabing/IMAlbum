package com.opensource.configure;

import android.app.Application;
import android.graphics.Bitmap;
import com.imalbumlib.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.opensource.configure.imgload.ResizeBitmapDisplayer;


public class Configure {
	private static Configure mConfigure;
	public static Application mApplication;
	public static final String APPLICATIONS_CACHE_DIR = "/huixin/applications_cache/";
	
	
	public  DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageOnFail(R.drawable.default_error) // resource or drawable
	.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
	// 设置图片在下载前是否重置，复位
	.resetViewBeforeLoading(true)
	.cacheInMemory(false)
	.displayer(new ResizeBitmapDisplayer(getResizePix(), getResizePix(), ResizeBitmapDisplayer.fixXY))
	.cacheOnDisc(false)
	.bitmapConfig(Bitmap.Config.RGB_565)
	.considerExifParams(true)//
	.imageScaleType(ImageScaleType.EXACTLY)
	.build();
	
	public  DisplayImageOptions options_image = new DisplayImageOptions.Builder()
	.showImageOnFail(R.drawable.default_error) // resource or drawable
	.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
	// 设置图片在下载前是否重置，复位
	.resetViewBeforeLoading(true)
	.cacheInMemory(false)
	.cacheOnDisk(false)
	.bitmapConfig(Bitmap.Config.RGB_565)
	.considerExifParams(true)//
	.imageScaleType(ImageScaleType.EXACTLY)
	.build();
	
	
	private int getResizePix()
	{
		int mImageSize = 80;
		if(null != mApplication)
		mImageSize = mApplication.getResources().getDimensionPixelOffset(R.dimen.folder_cover_size);
		return mImageSize;
	}
	
	public String getFileUrl(String absPath)
	{
		return "file://" + absPath;
	}
	
	public static Configure getInstance()
	{
		if(null == mConfigure)
		{
			mConfigure = new Configure();
		}
		return mConfigure;
	}
	
}
