package com.albumsimple.activity;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import com.albumsimple.R;
import com.opensource.configure.IntentUtils;
import com.opensource.multi_image_selector.adapter.MultiImageSelectorActivity;

/**
 @Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
 <activity android:name="com.huika.huixin.control.me.activity.PersonalInfoAct" 
            android:launchMode="singleTask" 
            android:configChanges="orientation|keyboardHidden|screenSize"
            >
        </activity> 
 
  
  
 * @Description: 选择图片的act
 * @author zhangjianlin
 * @date 2015-9-24 下午2:06:28
 */
public abstract class BaseSelectPicAct extends Activity {

	private final int CROPCAMARA_TAKEPHOTO = 1003;
	private final int CROPCAMARA_SELECTFROMALBUM = 1004;

	/** 从 拍照中选择 */
	private final int ACTIVITY_RESULT_CROPCAMARA_WITH_DATA = 1;
	private File mPicFile; // 选择图片路径
	private String picPath;

	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) { return; }
		switch (requestCode) {
			case IntentUtils.REQUEST_IMAGE: // 选择照片
				/** add by zjl */
				if (null != data) {
					ArrayList<String> resultList = data
							.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
					hasSelectPicsNoCrop(resultList);
					/* if (null != resultList) {
					 * 
					 * } */
				}
				break;

			case ACTIVITY_RESULT_CROPCAMARA_WITH_DATA: // 拍照
				/* if (mPicFile.exists()) {
				 * 
				 * } */
				if (mPicFile == null && !TextUtils.isEmpty(picPath)) mPicFile = new File(picPath);
				if (null != mPicFile && mPicFile.exists()) {
					hasTakePhotoPic(mPicFile);
				}
				break;
			case IntentUtils.REQUEST_CROPIMAGE:
				if (null == data) { return; }
				if (TextUtils.isEmpty(mPicFile.toString()) || !mPicFile.exists()) {
					Toast.makeText(this, "没有选择图片", Toast.LENGTH_LONG).show();
					return;
				}
				String mPicNativePath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/image" + mPicFile.getName();
				try {
					FilecacheTool.copyFile(mPicFile.getAbsolutePath(), mPicNativePath);
				} catch (Exception e) {
					e.printStackTrace();
				}

				hasPicsCroped(mPicNativePath);
				break;
		}
	};

	protected void selectPhoto() {
		ImageTool.jumpToSelectNoCropImg(this);
	}

	protected void selectPhotoAndCrop() {
		mPicFile = ImageTools.initTempFile();
		/* photoIntent = ImageTools.cropPhotoOfCompressFromGalleryIntent(Uri.fromFile(mPicFile)); startActivityForResult(photoIntent, Constant.ACTIVITY_RESULT_CROPIMAGE_WITH_DATA); */
		ImageTool.jumpToSelecAndCropImg(this, mPicFile.toString(), 300, 300, 600, 600, true);
	}

	/** 拍照 */
	protected void takePhoto() {
		if (!ImageTools.isSDCardExist()) {
			Toast.makeText(this, "没有内存卡", Toast.LENGTH_LONG).show();
			return;
		}
		mPicFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
				+ System.currentTimeMillis() + ".jpg");
		picPath = mPicFile.getAbsolutePath();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPicFile));
		startActivityForResult(intent, ACTIVITY_RESULT_CROPCAMARA_WITH_DATA);
	}

	protected abstract void hasSelectPicsNoCrop(ArrayList<String> resultList);

	protected abstract void hasTakePhotoPic(File mPicFile);

	protected abstract void hasPicsCroped(String mPicNativePath);

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("picPathCache", picPath);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		picPath = savedInstanceState.getString("picPathCache");
		super.onRestoreInstanceState(savedInstanceState);
	}
}
