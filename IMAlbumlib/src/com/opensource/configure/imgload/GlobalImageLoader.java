package com.opensource.configure.imgload;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class GlobalImageLoader {
	
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	public static GlobalImageLoader mGlobalImageLoader;//原来是interface类，只声明了几个display函数
	public static GlobalImageLoader getIntance()
	{
		if(null == mGlobalImageLoader)
		{
			mGlobalImageLoader = new GlobalImageLoader();
		}
		return mGlobalImageLoader;
	}
	
	public Bitmap loadImageSync(String url)
	{
		Bitmap bitmap = ImageLoader.getInstance().loadImageSync(url);
		return bitmap;
	}
	

	public void displayImg(ImageView view, String url, ImageLoadingListener listener) {
		if(null == listener)
			ImageLoader.getInstance().displayImage(url, view, animateFirstListener);
		else{
			ImageLoader.getInstance().displayImage(url, view, listener);
		}
	}

	//
	public void dispalyImg(ImageView imageView, String uri, DisplayImageOptions options) {
		ImageLoader.getInstance().displayImage(uri, imageView, options);
	}
	
	//
	public void dispalyImg(ImageView imageView, String uri, ImageLoadingListener listener, DisplayImageOptions options) {
		ImageLoader.getInstance().displayImage(uri, imageView, options, listener);
	}

}
