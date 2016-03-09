package com.opensource.cropImage;

import android.R.integer;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

/**
 * @Description: 截图相关的区域
 * @author zhangjianlin
 * @date 2015-8-19 下午4:41:43
 */
public class CropImageBean {
	private int drawableWidth;// 图片的宽
	private int drawableHight;// 图片的高
	private int halfTopPosition, halfTopPositionW;
	private RectF bitmapViewRect = new RectF();
	public Rect bitmapViewRectOrg = new Rect();

	private int minLimitX = 10;
	private int minLimitY = 10;

	public void setMinLimitXY(int minLimitX, int minLimitY) {
		this.minLimitX = minLimitX;
		this.minLimitY = minLimitY;
	}

	public Rect getImageViewRect() {
		Rect mRect = new Rect();
		mRect.left = (int) bitmapViewRect.left;
		mRect.top = (int) bitmapViewRect.top;
		mRect.bottom = (int) bitmapViewRect.bottom;
		mRect.right = (int) bitmapViewRect.right;
		return mRect;
	}

	public void setImageWH(int drawableWidth, int drawableHight, int width,
			int height) {
		if (this.drawableWidth == 0)
			this.drawableWidth = drawableWidth;
		if (this.drawableHight == 0)
			this.drawableHight = drawableHight;
		if (this.halfTopPosition == 0)
			halfTopPosition = height;
		if (this.halfTopPositionW == 0)
		{
			halfTopPositionW = width;
			bitmapViewRectOrg.set(0, 0, drawableWidth, drawableHight);
		}
	}

	public void setBitmapViewRect(RectF zoomRect, float zoomSize,
			int translateLeft, int translateTop) {
		this.bitmapViewRect.left = translateLeft - zoomRect.left
				* drawableWidth * zoomSize + getHalfWPosition(zoomSize);
		this.bitmapViewRect.right = this.bitmapViewRect.left + drawableWidth
				* zoomSize;
		this.bitmapViewRect.top = translateTop - zoomRect.top * drawableHight
				* zoomSize + getHalfPosition(zoomSize);
		this.bitmapViewRect.bottom = bitmapViewRect.top + drawableHight
				* zoomSize;
		Log.d("bitmapViewRect", bitmapViewRect.toString());
	}

	private int getHalfWPosition(float zoomSize) {
		int tmp = (int) ((halfTopPositionW - drawableWidth * zoomSize) / 2);
		return Math.max(tmp, 0);
	}

	private int getHalfPosition(float zoomSize) {
		int tmp = (int) ((halfTopPosition - drawableHight * zoomSize) / 2);
		return Math.max(tmp, 0);
	}

	public Rect getUnionRect(Rect drawRect) {
		Rect mRect = new Rect();
		Rect mBitmapRect = new Rect();
		mBitmapRect.left = (int) bitmapViewRect.left;
		mBitmapRect.right = (int) bitmapViewRect.right;
		mBitmapRect.top = (int) bitmapViewRect.top;
		mBitmapRect.bottom = (int) bitmapViewRect.bottom;
		if (mRect.setIntersect(drawRect, mBitmapRect)) {
			return mRect;
		}else{
			int dx = 0;
			int dy = 0;
			if(drawRect.left > mBitmapRect.right){
				dx = drawRect.left - mBitmapRect.right + 20;
			}
			if(mBitmapRect.left > drawRect.right){
				dx = drawRect.right - mBitmapRect.left - 20;
			}
			if(drawRect.top > mBitmapRect.bottom){
				dy = drawRect.top - mBitmapRect.bottom + 20;
			}
			
			if(mBitmapRect.top > drawRect.bottom){
				dy = drawRect.bottom - mBitmapRect.top - 20;
			}
			
			mBitmapRect.offset(dx, dy);
			bitmapViewRect.offset(dx, dy);
			mRect.setIntersect(drawRect, mBitmapRect);
		}		

		return mRect;
	}

	public boolean isAllowMovePre(float dx, float dy, Rect drawRect) {
		RectF mRectF = new RectF();
		mRectF.set(bitmapViewRect);
		RectF drawRectF = new RectF(drawRect);
		if(!mRectF.setIntersect(mRectF, drawRectF))return true;
		mRectF.offset(dx, dy);
		if (mRectF.setIntersect(mRectF, drawRectF)) {
			if (mRectF.width() > minLimitX && mRectF.height() > minLimitY) {
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * 平移是否被允许
	 * 
	 * @return
	 */
	public boolean isAllowMove(int imageViewWOrH, int transXorY) {
		if ((imageViewWOrH) > Math.abs(transXorY))
			return true;
		return false;
	}
}
