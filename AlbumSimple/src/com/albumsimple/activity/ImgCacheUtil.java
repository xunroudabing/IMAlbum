package com.albumsimple.activity;

import java.io.File;
import java.io.IOException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * @description：
 *               如果图片显示简单的话可以不用UIL显示，直接用Velloy自带的简单显示图片；
 *               UniversalImageLoad加载显示类
 *               注意事项，必须在Application初始化配置一下；
 *               ImageLoader.getInstance().init(ImgCacheUtil.getInstance().getImgLoaderConfig());
 * @author samy
 * @date 2014年11月11日 下午2:07:16
 */
public class ImgCacheUtil {
	private static ImgCacheUtil imgCacheUtil = null;
	private Context mContext;

	public static ImgCacheUtil getInstance(Context context) {
		if (null == imgCacheUtil) {
			synchronized (ImgCacheUtil.class) {
				if (null == imgCacheUtil) {
					imgCacheUtil = new ImgCacheUtil(context);
				}
			}
		}
		return imgCacheUtil;
	}

	private ImgCacheUtil(Context context) {
		this.mContext = context;
		File cacheDir = StorageUtils.getOwnCacheDirectory(mContext, "Android/data" + File.separator + mContext.getPackageName() + File.separator
				+ "cache/images");
		DisplayImageOptions options = new DisplayImageOptions.Builder()// 开始构建, 显示的图片的各种格式
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				.cacheInMemory(true)// 开启内存缓存
				.cacheOnDisk(true) // 开启硬盘缓存
				.displayer(new SimpleBitmapDisplayer())// 正常显示一张图片　
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型;使用.bitmapConfig(Bitmap.config.RGB_565)代替ARGB_8888;
				.considerExifParams(true)// 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.EXACTLY)// 缩放级别
				.build();// 构建完成
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY - 1);
		config.defaultDisplayImageOptions(options);//
		config.denyCacheImageMultipleSizesInMemory();
//		config.memoryCache(new WeakMemoryCache());
		config.memoryCacheSizePercentage(40);
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		try {
			config.diskCache(new LruDiskCache(cacheDir, new Md5FileNameGenerator(), 50 * 1024 * 1024));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// config.writeDebugLogs(); // Remove for release app
		ImageLoaderConfiguration mconfig = config.build();
		ImageLoader.getInstance().init(mconfig);// 全局初始化此配置;mageLoaderConfiguration必须配置并且全局化的初始化这个配置ImageLoader.getInstance().init(config); 否则也会出现错误提示
	}

	/**
	 * String imageUri = "http://site.com/image.png"; // from Web
	 * String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
	 * String imageUri = "content://media/external/audio/albumart/13"; // from content provider
	 * String imageUri = "assets://image.png"; // from assets
	 * String imageUri = "drawable://" + R.drawable.image; // from drawables (only images, non-9patch)
	 * 加载自定义配置的图片
	 * 
	 * @param url
	 * @param imageView
	 * @param options
	 */
	public void displayImage(String url, ImageView imageView) {
		ImageLoader.getInstance().displayImage(url, imageView);
	}

	/**
	 * @param url
	 * @param imageView
	 * @param listener
	 *            监听图片下载情况
	 */
	public void displayImage(String url, ImageView imageView, ImageLoadingListener listener) {
		ImageLoader.getInstance().displayImage(url, imageView, listener);
	}

	
	/**
	 * From Drawable
	 * 
	 * @param imageUri
	 * @param imageView
	 * @throws IOException
	 */
	public void displayImageFromDrawable(Context context, String imageUri, ImageView imageView) {
		String drawableIdString = Scheme.DRAWABLENAME.crop(imageUri);
		int resID = context.getResources().getIdentifier(drawableIdString, "drawable", context.getPackageName());

		if (imageView != null) {
			imageView.setImageResource(resID);
		}
		return;
	}

}