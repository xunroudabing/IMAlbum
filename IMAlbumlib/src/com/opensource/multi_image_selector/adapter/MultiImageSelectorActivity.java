package com.opensource.multi_image_selector.adapter;

import java.io.File;
import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import com.imalbumlib.R;
import com.opensource.TiTouchImageView.ImageDetailCheckPicsAct;
import com.opensource.TiTouchImageView.LocateImagePagerUrl;
import com.opensource.configure.IntentUtils;
import com.opensource.cropImage.CropImageAct;
import com.opensource.multi_image_selector.adapter.bean.Image;

/**
 * 多图选择
 * Created by Nereo on 2015/4/7.
 */
public class MultiImageSelectorActivity extends FragmentActivity implements MultiImageSelectorFragment.Callback {

	/** 最大图片选择次数，int类型，默认9 */
	public static final String EXTRA_SELECT_COUNT = "max_select_count";

	/** 发送按钮的标题  如完成或发送 */
	public static final String EXTRA_SUBMIT_TITLE = "extra_submit_title";

	/** 图片选择模式，默认多选 */
	public static final String EXTRA_SELECT_MODE = "select_count_mode";
	/** 是否显示相机，默认显示 */
	public static final String EXTRA_SHOW_CAMERA = "show_camera";
	/** 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合  */
	public static final String EXTRA_RESULT = "select_result";
	/** 默认选择集 */
	public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

	/** 单选 */
	public static final int MODE_SINGLE = 0;
	/** 多选 */
	public static final int MODE_MULTI = 1;

	public String submitTitle;

	private ArrayList<String> resultList = new ArrayList();
	private Button mSubmitButton;
	private int mDefaultCount;

	/** 截图相关*/
	private boolean isScaleImg = false;
	private boolean isJumpToCheckPics = false;

	private String photoResultPath;
	private int scaleW = 200, scaleH = 200;// 截图区域的宽高

	private MultiImageSelectorFragment multiImageSelectorFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_default);

		Intent intent = getIntent();
		mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 9);
		int mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
		boolean isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
		submitTitle = intent.getStringExtra(EXTRA_SUBMIT_TITLE);

		/** 截图相关*/
		isScaleImg = intent.getBooleanExtra(CropImageAct.RESIZEBIT, true);
		isJumpToCheckPics = intent.getBooleanExtra(MultiImageSelectorFragment.EXTRA_JUMPTOCHECKPICS, false);
		photoResultPath = getIntent().getStringExtra(CropImageAct.RESULT);
		scaleW = getIntent().getIntExtra(CropImageAct.SCALEW, 200);
		scaleH = getIntent().getIntExtra(CropImageAct.SCALEH, 200);
		int limitWidth = getIntent().getIntExtra(CropImageAct.LIMITW, 600);
		int limitHight = getIntent().getIntExtra(CropImageAct.LIMITH, 600);
		if (mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
			resultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
		}

		Bundle bundle = new Bundle();
		bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
		bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
		bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
		bundle.putStringArrayList(MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST, resultList);

		bundle.putBoolean(MultiImageSelectorFragment.MODE_SCALE_BOOLEAN, isScaleImg);

		// TODO 2015 11 20 ImageDetailCheckPicsAct
		bundle.putBoolean(MultiImageSelectorFragment.EXTRA_JUMPTOCHECKPICS, isJumpToCheckPics);
		bundle.putString(EXTRA_SUBMIT_TITLE, getSubmitStr());
		bundle.putString(MultiImageSelectorFragment.MODE_SCALE_PATH, photoResultPath);
		bundle.putInt(MultiImageSelectorFragment.MODE_SCALE_WIDTH, scaleW);
		bundle.putInt(MultiImageSelectorFragment.MODE_SCALE_HIGHT, scaleH);
		bundle.putInt(CropImageAct.LIMITW, limitWidth);
		bundle.putInt(CropImageAct.LIMITH, limitHight);
		multiImageSelectorFragment = (MultiImageSelectorFragment) Fragment.instantiate(this,
				MultiImageSelectorFragment.class.getName(), bundle);
		getSupportFragmentManager().beginTransaction().add(R.id.image_grid, multiImageSelectorFragment).commit();

		// 返回按钮
		findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});

		// 完成按钮
		mSubmitButton = (Button) findViewById(R.id.commit);
		if (mode == MODE_SINGLE) mSubmitButton.setVisibility(View.GONE);
		if (resultList == null || resultList.size() <= 0) {
			mSubmitButton.setText(getSubmitStr());
			mSubmitButton.setEnabled(false);
		} else {
			mSubmitButton.setText(getSubmitStr() + "(" + resultList.size() + "/" + mDefaultCount + ")");
			mSubmitButton.setEnabled(true);
		}
		mSubmitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (resultList != null && resultList.size() > 0) {
					// 返回已选择的图片数据
					Intent data = new Intent();
					data.putStringArrayListExtra(EXTRA_RESULT, resultList);
					setResult(RESULT_OK, data);
					finish();
				}
			}
		});
	}

	private String getSubmitStr() {
		if (TextUtils.isEmpty(submitTitle)) {
			submitTitle = getResources().getString(R.string.album_compile);
		}
		return submitTitle;
	}

	@Override
	public void onSingleImageSelected(String path) {
		Intent data = new Intent();
		resultList.add(path);
		data.putStringArrayListExtra(EXTRA_RESULT, resultList);
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	public void onImageSelected(String path) {
		if (!resultList.contains(path)) {
			resultList.add(path);
		}
		// 有图片之后，改变按钮状态
		if (resultList.size() > 0) {
			mSubmitButton.setText(getSubmitStr() + "(" + resultList.size() + "/" + mDefaultCount + ")");
			if (!mSubmitButton.isEnabled()) {
				mSubmitButton.setEnabled(true);
			}
		}
	}

	@Override
	public void onImageUnselected(String path) {
		if (resultList.contains(path)) {
			resultList.remove(path);
			mSubmitButton.setText(getSubmitStr() + "(" + resultList.size() + "/" + mDefaultCount + ")");
		} else {
			mSubmitButton.setText(getSubmitStr() + "(" + resultList.size() + "/" + mDefaultCount + ")");
		}
		// 当为选择图片时候的状态
		if (resultList.size() == 0) {
			mSubmitButton.setText(getSubmitStr());
			mSubmitButton.setEnabled(false);
		}
	}

	@Override
	public void onCameraShot(File imageFile) {
		if (imageFile != null) {
			Intent data = new Intent();
			resultList.add(imageFile.getAbsolutePath());
			data.putStringArrayListExtra(EXTRA_RESULT, resultList);
			setResult(RESULT_OK, data);
			finish();
		}
	}

	@Override
	public void onPreviewBtnClock() {
		// TODO Auto-generated method stub
		if (resultList.size() == 0) return;
		ArrayList<LocateImagePagerUrl> mImageList = new ArrayList<LocateImagePagerUrl>();
		for (int i = 0; i < resultList.size(); i++) {
			LocateImagePagerUrl mLocateImagePagerUrl = new LocateImagePagerUrl();
			mLocateImagePagerUrl.setLocateUrl(resultList.get(i));
			mImageList.add(mLocateImagePagerUrl);
		}
		if(isJumpToCheckPics)
		{
			
			IntentUtils.jumpToPreViewImageChecked(this, mImageList, 0, resultList, mDefaultCount, submitTitle);
		}else{
			IntentUtils.jumpToPreViewImage(this, mImageList, "");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) {
			if (requestCode == IntentUtils.REQUEST_CROPIMAGE) {
				String pathString = data.getStringExtra(CropImageAct.RESULT);
				Intent mdata = new Intent();
				resultList.add(pathString);
				mdata.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT, resultList);
				setResult(RESULT_OK, mdata);
				finish();
			} else if (requestCode == ImageDetailCheckPicsAct.REQUEST_CHECKFILE) {
				boolean isSendBtn = data.getBooleanExtra(ImageDetailCheckPicsAct.RESULTYPE, false);
				ArrayList<String> mArrayList = (ArrayList<String>) data
						.getSerializableExtra(ImageDetailCheckPicsAct.RESULT);
				if (isSendBtn) {
					if (mArrayList != null && mArrayList.size() > 0) {
						// 返回已选择的图片数据
						Intent mdate = new Intent();
						mdate.putStringArrayListExtra(EXTRA_RESULT, mArrayList);
						setResult(RESULT_OK, mdate);
						finish();
					}
				} else {
					
					if (mArrayList != null) {
						resultList.clear();
						resultList.addAll(mArrayList);
						multiImageSelectorFragment.resetSelectPics(resultList, true);
						// 当为选择图片时候的状态
						if (resultList.size() > 0) {
							mSubmitButton.setText(getSubmitStr() + "(" + resultList.size() + "/" + mDefaultCount + ")");
							mSubmitButton.setEnabled(true);
						} else {
							mSubmitButton.setText(getSubmitStr());
							mSubmitButton.setEnabled(false);
						}
					}
				}
			}

		} else {
			setResult(RESULT_CANCELED);
			finish();
		}
	}

}
