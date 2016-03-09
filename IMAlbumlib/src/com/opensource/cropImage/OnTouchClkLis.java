package com.opensource.cropImage;

import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
/**
 * @Description: touch的监听
 * @author zhangjianlin
 * @date 2015-8-19 下午4:41:07
 */
public class OnTouchClkLis implements OnTouchListener {
	public float lastX = 0;
	public float lastY = 0;
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;
	private static float XJUMP = 0f;// 灵敏度
	private static float YJUMP = 0f;// 灵敏度
	private OnTouchEventActions mEventActions;

	private boolean isDispatchTouchEvent = true;
	
	public void setIsDispatchTouchEvent(boolean isDispatchTouchEvent)
	{
		this.isDispatchTouchEvent = isDispatchTouchEvent;
	}
	
	public void setmEventActions(OnTouchEventActions mEventActions) {
		this.mEventActions = mEventActions;
	}

	public interface OnTouchEventActions {
		public void onActionUp();//点击抬起
		
		public void onActionDown();//点击

		public void onActionPointUp();//两只手放开

		public void onActionPointDown();//两只手按下

		public void onActionPointMove(MotionEvent event);//拖动(移动)事件
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			lastX = event.getX();
			lastY = event.getY();
			mode = DRAG;
			if (mEventActions != null)
				mEventActions.onActionDown();
			break;
		case MotionEvent.ACTION_UP:
			if (mEventActions != null)
				mEventActions.onActionUp();
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			if (mEventActions != null)
				mEventActions.onActionPointUp();
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			float oldDist = spacing(event);
			if (oldDist > 5f) {
				if (mEventActions != null)
					mEventActions.onActionPointDown();
				mode = ZOOM;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				int dx = (int) (event.getRawX() - lastX);// dx为在屏幕的x轴上移动的距离
				int dy = (int) (event.getRawY() - lastY);
				if (((Math.abs(dx) > XJUMP) || (Math.abs(dy) > YJUMP))) {
					if (mEventActions != null)
						mEventActions.onActionPointMove(event);
				}
			}
			lastX = event.getX();
			lastY = event.getY();
			break;
		}
		return isDispatchTouchEvent;
	}

	// 计算移动距离
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

}
