package com.opensource.multi_image_selector.adapter.adapter;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

public class OnItemChildClickListener implements OnClickListener {
	// 点击类型索引，对应前面的CLICK_INDEX_xxx
	private int clickIndex;
	// 点击列表位置
	private int position;
	// activity 消息传递
	private Handler mHandle;

	public OnItemChildClickListener(int clickIndex, int position, Handler mHandle) {
		this.clickIndex = clickIndex;
		this.position = position;
		this.mHandle = mHandle;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 创建Message并填充数据，通过mHandle联系Activity接收处理
		Message msg = new Message();
		msg.what = clickIndex;
		msg.arg1 = position;
		mHandle.sendMessage(msg);
	}
}
