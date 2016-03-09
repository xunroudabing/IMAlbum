package com.opensource.TiTouchImageView;

import java.io.File;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.imalbumlib.R;
import com.ortiz.touch.TouchImageView;


/**
 * 基础的图片放大类 ==> 可当做布局来做  用match熟悉，其他要添加的布局，放在RelativeLayout中
 * @description：
 * @author zhangjianlin (990996641)
 * @date 2015年4月30日 下午5:41:34
 */
public class BaseImageZoomFrameLayout extends FrameLayout{

	

	public BaseImageZoomFrameLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public BaseImageZoomFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
		init(context);
	}

	public TouchImageView base_imageView;

	private void init(Context context) {
		base_imageView = (TouchImageView) LayoutInflater.from(context).inflate(R.layout.base_zoomimagelayout, null);
		base_imageView.setOnLongClickListener(onLongClickListener);
		base_imageView.setOnClickListener(mClickListener);
		addView(base_imageView);
	}

	private OnLongClickListener onLongClickListener = new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			if (null != mOnImageZoomLongClick) mOnImageZoomLongClick.longClickListener(v);
			return false;
		}
	};
	
	private OnClickListener mClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (null != mOnImageZoomLongClick) mOnImageZoomLongClick.onClickImageListener(v);
		}
	};

	private OnImageZoomLongClick mOnImageZoomLongClick;

	public void setOnImageZoomLongClick(OnImageZoomLongClick mOnImageZoomLongClick) {
		this.mOnImageZoomLongClick = mOnImageZoomLongClick;
	}

	public interface OnImageZoomLongClick {
		void longClickListener(View v);
		void onClickImageListener(View v);
		
	}

	public void addMatchParentView(View v, int index) {
		addView(v, index);
	}

	/**
	 * 将当前图片设置回原来的值
	 * @description：
	 * @author zhangjianlin (990996641)
	 * @date 2015年4月30日 下午4:39:10
	 */
	public void resetZoomToDefaultSize() {
		base_imageView.resetZoom();
	}

	/**
	 * 获取当前放大的比例
	 * @description：
	 * @author zhangjianlin (990996641)
	 * @date 2015年4月30日 下午4:38:45
	 */
	public float getCurrentZoom() {
		return base_imageView.getCurrentZoom();
	}

	/**
	 * @param maxZoomSize 可设置大于或等于1，minZoomSize必需大于0小于或等maxZoomSize
	 * @description：
	 * @author zhangjianlin (990996641)
	 * @date 2015年4月30日 下午4:34:42
	 */
	public void setMaxMinZoom(float maxZoomSize, float minZoomSize) {
		if (maxZoomSize >= 1) base_imageView.setMaxZoom(maxZoomSize);
		if (minZoomSize > 0 && base_imageView.getMaxZoom() >= minZoomSize) base_imageView.setMinZoom(minZoomSize);
	}

	/**
	 * 获取滑动的位置
	 * @description：
	 * @author zhangjianlin (990996641)
	 * @date 2015年4月30日 下午4:36:28
	 */
	public PointF getScrollPotion() {
		return base_imageView.getScrollPosition();
	}

	/**
	 * 获去可视区域
	 * @description：
	 * @author zhangjianlin (990996641)
	 * @date 2015年4月30日 下午4:36:42
	 */
	public RectF getZoomRect() {
		RectF rect = base_imageView.getZoomedRect();
		return rect;
	}

	public void setImageBitmap(File filePath)
	{
		setImageBitmap(Uri.fromFile(filePath));
	}
	
	public void setImageBitmap(Uri uri)
	{
		base_imageView.setImageURI(uri);
	}
	
	public void setImageBitmap(Bitmap mBitmap)
	{
		base_imageView.setImageBitmap(mBitmap);
	}
	
	public void scrollToPotion(int x, int y) {
		base_imageView.scrollTo(x, y);
	}
}
