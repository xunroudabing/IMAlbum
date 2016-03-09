package com.opensource.multi_image_selector.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.imalbumlib.R;

/**
 * 自定义选择图片的activity
 * @description：
 * @author zhangjianlin (990996641)
 * @date 2015年4月28日 上午10:52:22
 */
public abstract class CustomerSelectActivity extends FragmentActivity implements MultiImageSelectorFragment.Callback{
	
	int mDefaultCount = 5;
	int mode = MultiImageSelectorActivity.MODE_MULTI;
	boolean isShow = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // customer logic here...
    	mDefaultCount = getIntent().getIntExtra(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, 5);
    	mode = getIntent().getIntExtra(MultiImageSelectorFragment.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
    	isShow = getIntent().getBooleanExtra(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, true);
    	
        Bundle bundle = new Bundle();
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
        // Add fragment to your Activity
        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_grid, Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle))
                .commit();
    }
  /*  
    @Override
    public void onSingleImageSelected(String path) {
        // When select mode set to MODE_SINGLE, this method will received result from fragment
    }

    @Override
    public void onImageSelected(String path) {
        // You can specify your ActionBar behavior here 
    }

    @Override
    public void onImageUnselected(String path) {
        // You can specify your ActionBar behavior here 
    }

    @Override
    public void onCameraShot(File imageFile) {
        // When user take phone by camera, this method will be called.
    }*/
}