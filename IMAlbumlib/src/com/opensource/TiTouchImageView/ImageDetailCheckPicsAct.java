package com.opensource.TiTouchImageView;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.imalbumlib.R;
import com.opensource.TiTouchImageView.ImageDetailViewFrameLayOut.IImageActionCallback;
import com.opensource.TiTouchImageView.bean.ImagePagerUri;
import com.opensource.multi_image_selector.adapter.MultiImageSelectorActivity;

public class ImageDetailCheckPicsAct extends ImageDetailAct implements OnClickListener {
	public static HashMap<String , Object> mExtraHashMap = new HashMap<String, Object>();//非intent传递数据，intent传值data大小有限, IntentUtils.jumpToPreViewImageChecked

	/**
	 * 放外部变量
	 */
	public static void putValue(String key, Object value)
	{
		mExtraHashMap.clear();
		mExtraHashMap.put(key, value);
	}
	
	/**
	 * 外部传值的方法
	 */
	@Override
	protected ArrayList<ImagePagerUri> getExtraImageList() {
		return (ArrayList<ImagePagerUri>) mExtraHashMap.get("imageListBean");
	}
	
	public final static String EXTRA_CHECKEDPICS = "EXTRA_CHECKEDPICS";
	public final static String RESULT = "REQUEST_CHECKFILE";
	public final static String RESULTYPE = "REQUEST_TYPE";
	public static final int REQUEST_CHECKFILE = 800;
	private ArrayList<String> resultList;
	private int maxSelectCount;
	public String submitTitle;

	private int PageNum = 0;
	private View childView = null;
	private ImageView checkmark;
	private LinearLayout ll_checkbtn;
	private TextView tv_backtitle;
	private Button commit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		resultList = (ArrayList<String>) getIntent().getSerializableExtra(EXTRA_CHECKEDPICS);
		maxSelectCount = getIntent().getIntExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 0);
		submitTitle = getIntent().getStringExtra(MultiImageSelectorActivity.EXTRA_SUBMIT_TITLE);
		if (resultList == null) {
			resultList = new ArrayList<String>();
		}
		childView = LayoutInflater.from(this).inflate(R.layout.activity_checkedpic, null);
		tv_backtitle = (TextView) childView.findViewById(R.id.tv_backtitle);
		ll_checkbtn = (LinearLayout) childView.findViewById(R.id.ll_checkbtn);
		checkmark = (ImageView) childView.findViewById(R.id.checkmark);
		commit = (Button) childView.findViewById(R.id.commit);
		childView.findViewById(R.id.btn_back).setOnClickListener(this);
		commit.setOnClickListener(this);
		ll_checkbtn.setOnClickListener(this);
		setCommitBtn();
		super.onCreate(savedInstanceState);
		addView(childView);
	}

	protected void setAction() {
		mIImageActionCallback = new IImageActionCallback() {

			@Override
			public void onPageSelected(int index) {
				// TODO Auto-generated method stub
				PageNum = index;
				tv_backtitle.setText(index + 1 + "/" + shop_gallery_relayout.getPageSize());
				if (resultList.contains(shop_gallery_relayout.getImageUriList().get(PageNum).getLocalPath())) {
					// 设置选中状态
					checkmark.setImageResource(R.drawable.btn_selected);
				} else {
					// 未选择
					checkmark.setImageResource(R.drawable.btn_unselected);
				}
			}

			@Override
			public void onImageLongClick() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGifLongClick() {
				// TODO Auto-generated method stub

			}

			@Override
			public void OnGifOnclick() {}

			@Override
			public void OnImageOnclick() {}
		};
	}

	private void setCommitBtn() {
		// 当为选择图片时候的状态
		if (resultList.size() > 0) {
			commit.setText(getSubmitStr() + "(" + resultList.size() + "/" + maxSelectCount + ")");
			commit.setEnabled(true);
		} else {
			commit.setText(getSubmitStr());
			commit.setEnabled(true);
		}
		
	}

	private String getSubmitStr() {
		if (TextUtils.isEmpty(submitTitle)) {
			submitTitle = getResources().getString(R.string.album_compile);
		}
		return submitTitle;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.ll_checkbtn) {
			if (resultList.contains(shop_gallery_relayout.getImageUriList().get(PageNum).getLocalPath())) {
				resultList.remove(shop_gallery_relayout.getImageUriList().get(PageNum).getLocalPath());
				checkmark.setImageResource(R.drawable.btn_unselected);
				setCommitBtn();
			} else {
				if (maxSelectCount > resultList.size()) {
					resultList.add(shop_gallery_relayout.getImageUriList().get(PageNum).getLocalPath());
					checkmark.setImageResource(R.drawable.btn_selected);
					setCommitBtn();
				} else {
					Toast.makeText(this, getResources().getString(R.string.msg_amount_limit), Toast.LENGTH_LONG).show();
				}
			}
		} else if(id == R.id.commit){
			if(resultList.size() == 0)
			{
				resultList.add(shop_gallery_relayout.getImageUriList().get(PageNum).getLocalPath());
				checkmark.setImageResource(R.drawable.btn_selected);
				setCommitBtn();
			}
			exitAndSaveResult(true);
		}else if(id == R.id.btn_back)
		{
			exitAndSaveResult(false);
		}else{
			
		}
	}
	
	public void exitAndSaveResult(boolean isSendBtn)
	{
		Intent data = new Intent();
		data.putExtra(RESULT, resultList);
		data.putExtra(RESULTYPE, isSendBtn);
		setResult(RESULT_OK, data);
		finish();
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitAndSaveResult(false);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	

}
