package com.opensource.configure.imgload;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

public class ResizeBitmapDisplayer implements BitmapDisplayer {

	public final static int fixX = 1000;// 以x大小为标准，安比例缩放XY，记得用scollview
	public final static int fixXY = 1002;// 以xy大小标准缩放

	private int targetWidth;
	private int targetHeight;
	private int scaleType = fixXY;

	@Override
	public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
		// TODO Auto-generated method stub
		if (!(imageAware instanceof ImageViewAware)) { throw new IllegalArgumentException(
				"ImageAware should wrap ImageView. ImageViewAware is expected."); }

		imageAware.setImageBitmap(transform(bitmap));
	}

	public ResizeBitmapDisplayer(int targetWidth, int targetHeight, int scaleType) {
		// TODO Auto-generated constructor stub
		this.targetWidth = targetWidth;
		this.targetHeight = targetHeight;
		this.scaleType = scaleType;

	}

	private Bitmap transform(Bitmap source) {
/*		int width = source.getWidth();
		int height = source.getHeight();
		// 设置想要的大小int newWidth = 500;int newHeight = 400;
		// 计算缩放比例
		float scaleWidth = ((float) targetWidth) / width;
		float scaleHeight = ((float) targetHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		if (scaleType == fixX) {
			matrix.postScale(scaleWidth, scaleWidth);// 得到新的图片
		} else {
			matrix.postScale(scaleWidth, scaleHeight);// 得到新的图片
		}
		source = Bitmap.createBitmap(source, 0, 0, width, height, matrix, true);
*/		
		//TODO  内存溢出问题
		source = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, true);
		return source;
	}
}
