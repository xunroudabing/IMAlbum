package com.opensource.cropImage;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * CropTouchImageView必需要调用setImageBitmap
 * @description：
 * @author zhangjianlin (990996641)
 * @date 2015年5月7日 上午9:43:17
 */
public class CropImageLoaderListener extends SimpleImageLoadingListener{
	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		// TODO Auto-generated method stub
		ImageView tmpImageView = (ImageView) view;
		tmpImageView.setImageBitmap(loadedImage);
	}
}
