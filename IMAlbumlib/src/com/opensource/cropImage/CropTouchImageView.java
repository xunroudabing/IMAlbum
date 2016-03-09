package com.opensource.cropImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import com.opensource.cropImage.OnTouchClkLis.OnTouchEventActions;
import com.ortiz.touch.TouchImageView;
/**
 * @Description:截图的view
 * @author zhangjianlin
 * @date 2015-8-19 下午4:40:35
 */
public class CropTouchImageView extends TouchImageView implements OnTouchEventActions {

	private int height, width;// 截屏区域宽高
	private int translateLeft = 0;// 平移操作
	private int translateTop = 0;// 平移操作
	private CropImageBean mBeanTmp;

	public void setCropWH(int widthTmp, int heightTmp) {
		this.width = widthTmp;
		this.height = heightTmp;
	}

	public Bitmap getCropBitmap(boolean isAbsSize, int limitWidth, int limitHight) {
		Matrix ffmatrix = null;

		mBitmapPoint = BitmapUtils.getInstance().resetBitmap(mBitmapPoint, mBeanTmp.bitmapViewRectOrg);
		Rect mRect = mBeanTmp.getUnionRect(drawRect);
		double x = (1.0 * (mRect.left - mBeanTmp.getImageViewRect().left) / mBeanTmp.getImageViewRect().width() * mBitmapPoint
				.getWidth());
		double y = (1.0 * (mRect.top - mBeanTmp.getImageViewRect().top) / mBeanTmp.getImageViewRect().height() * mBitmapPoint
				.getHeight());
		double width = ((mRect.width() - 2) / getCurrentZoom());
		double height = ((mRect.height() - 2) / getCurrentZoom());
		if (isAbsSize) {
			ffmatrix = new Matrix();
			float scaleWidth = ((float) limitWidth) / (int) width;
			float scaleHeight = ((float) limitHight) / (int) height;
			ffmatrix.postScale(scaleWidth, scaleHeight);
		}
		Bitmap mBitmap = Bitmap.createBitmap(mBitmapPoint, (int) x, (int) y, (int) width, (int) height, ffmatrix, true);
		return mBitmap;
	}

	public CropTouchImageView(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		this(context, attrs, 0);
	}

	public CropTouchImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	private final Paint outsidePaint = new Paint();
	private final Paint outlinePaint = new Paint();
	private float outlineWidth;
	private static final float OUTLINE_DP = 2f;
	private OnTouchClkLis mClkLis;

	// 初始化画笔等等，边框宽度
	private void init() {
		outsidePaint.setARGB(125, 50, 50, 50);
		outlinePaint.setStyle(Paint.Style.STROKE);
		outlinePaint.setAntiAlias(true);
		outlineWidth = dpToPx(OUTLINE_DP);
		mClkLis = new OnTouchClkLis();
		mClkLis.setmEventActions(this);
		mBeanTmp = new CropImageBean();
		setOnTouchListener(mClkLis);
	}

	private Bitmap mBitmapPoint;

	@Override
	public void setImageBitmap(Bitmap mBitmap) {
		// TODO Auto-generated method stub
		setMinZoom(0.5f);
		mBitmapPoint = mBitmap;
		setMaxZoom(5f);
		super.setImageBitmap(mBitmap);
	}

	private Rect drawRect; // Screen space

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.save();
		canvas.translate(translateLeft, translateTop);
		super.onDraw(canvas);
		canvas.restore();
		drawRects(canvas);
	}

	// 画边框外部透明的灰色背景
	// 画边框
	private void drawRects(Canvas canvas) {
		canvas.save();
		resetCropWH();
		int drawW = getDrawable().getIntrinsicWidth();
		int drawH = getDrawable().getIntrinsicHeight();
		if (drawW >= drawH) {
			if (drawW != getWidth()) {
				int tmpwW = getWidth();
				int tmpwH = (int) (drawH * (getWidth() * 1.0) / drawW);
				if (tmpwH > getHeight()) {
					tmpwW = (int) (tmpwW * (getHeight() * 1.0 / tmpwH));
					tmpwH = getHeight();
				}
				mBeanTmp.setImageWH(tmpwW, tmpwH, getWidth(), getHeight());
			} else {
				mBeanTmp.setImageWH(drawW, drawH, getWidth(), getHeight());
			}
		} else {
			if (drawH != getHeight()) {
				int tmphW = (int) (drawW * (getHeight() * 1.0 / drawH));
				int tmphH = getHeight();
				if (tmphW > getWidth()) {
					tmphH = (int) (tmphH * (getWidth() * 1.0 / tmphW));
					tmphW = getWidth();
				}
				mBeanTmp.setImageWH(tmphW, tmphH, getWidth(), getHeight());
			} else {
				mBeanTmp.setImageWH(drawW, drawH, getWidth(), getHeight());
			}
		}
		Log.d("setImageWH", getDrawable().getBounds().width() + "::" + getDrawable().getBounds().height());
		Log.d("bitmapWH", getWidth() + "::" + getHeight());

		if (null == drawRect) {
			drawRect = new Rect((getWidth() - width) / 2, (getHeight() - height) / 2, (getWidth() - width) / 2 + width,
					(getHeight() - height) / 2 + height);
		} else {
			drawRect.set((getWidth() - width) / 2, (getHeight() - height) / 2, (getWidth() - width) / 2 + width,
					(getHeight() - height) / 2 + height);
		}
		mBeanTmp.setBitmapViewRect(getZoomedRect(), getCurrentZoom(), translateLeft, translateTop);
		Path path = new Path();
		outlinePaint.setStrokeWidth(outlineWidth);
		path.addRect(new RectF(drawRect), Path.Direction.CW);
		outlinePaint.setColor(0xFF33B5E5);
		drawOutsideFallback(canvas);
		canvas.restore();
		canvas.drawPath(path, outlinePaint);
	}

	private void resetCropWH() {
		if (width > getWidth() && width > 0) this.width = getWidth();
		if (height > getHeight() && height > 0) this.height = width;
	}

	/* Fall back to naive method for darkening outside crop area */
	private void drawOutsideFallback(Canvas canvas) {
		canvas.drawRect(0, 0, canvas.getWidth(), drawRect.top, outsidePaint);
		canvas.drawRect(0, drawRect.bottom, canvas.getWidth(), canvas.getHeight(), outsidePaint);
		canvas.drawRect(0, drawRect.top, drawRect.left, drawRect.bottom, outsidePaint);
		canvas.drawRect(drawRect.right, drawRect.top, canvas.getWidth(), drawRect.bottom, outsidePaint);
	}

	/** dp 转换 */
	private float dpToPx(float dp) {
		return dp * this.getResources().getDisplayMetrics().density;
	}

	@Override
	public void onActionPointUp() {
		// TODO Auto-generated method stub
		mBeanTmp.setBitmapViewRect(getZoomedRect(), getCurrentZoom(), translateLeft, translateTop);
	}

	@Override
	public void onActionPointDown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActionPointMove(MotionEvent event) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		float dx = (float) (event.getX() - mClkLis.lastX + 0.5);
		float dy = (float) (event.getY() - mClkLis.lastY + 0.5);
		int transLeft = (int) (translateLeft + dx);
		int transtop = (int) (translateTop + dy);
		if (mBeanTmp.isAllowMove(getWidth(), transLeft) && mBeanTmp.isAllowMove(getHeight(), transtop)
				&& mBeanTmp.isAllowMovePre(dx, dy, drawRect)) {
			translateLeft = transLeft;
			translateTop = transtop;
			invalidate();
		}
	}
	
	public boolean isBitmapNull()
	{
		return (mBitmapPoint == null);
	}

	@Override
	public void onActionUp() {
	}

	@Override
	public void onActionDown() {
	}

}
