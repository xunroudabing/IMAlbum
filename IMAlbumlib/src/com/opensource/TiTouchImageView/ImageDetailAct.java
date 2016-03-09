package com.opensource.TiTouchImageView;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import com.imalbumlib.R;
import com.opensource.TiTouchImageView.ImageDetailViewFrameLayOut.IImageActionCallback;
import com.opensource.TiTouchImageView.bean.ImagePagerUri;

/**
 *  * BaseImagesViewPagerFrameLayout ==> ImageDetailViewFrameLayOut ==>ImageDetailAct ==> ImageDetailMoreAct
 * 滑动预览的基础类 ==> 逻辑类 ==>业务层
 * @Description:业务层
 * @author zhangjianlin
 * @date 2015-8-19 下午4:48:18
 */
public class ImageDetailAct extends Activity{
	protected ImageDetailViewFrameLayOut shop_gallery_relayout;
	public IImageActionCallback mIImageActionCallback;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gallery_image);
		shop_gallery_relayout = (ImageDetailViewFrameLayOut) findViewById(R.id.shop_gallery_relayout);
		ArrayList<ImagePagerUri> mImageList = getExtraImageList();
		String mPhotoCheck = getIntent().getStringExtra("imageDetailCheck");
		int checkIndex = getIntent().getIntExtra("imageDetailCheckPositon", -1);
		if(checkIndex >= mImageList.size())
			{checkIndex = 0;}
		setAction();
		shop_gallery_relayout.setIImageActionCallback(mIImageActionCallback);
		shop_gallery_relayout.setImages(mImageList, mPhotoCheck, checkIndex);
	}
	
	/**
	 * @Description:外部传值的方法
	 * @return
	 */
	protected ArrayList<ImagePagerUri> getExtraImageList()
	{
		return (ArrayList<ImagePagerUri>) getIntent().getSerializableExtra("imageListBean");
	}
	
	protected void setAction()
	{
		
	}
	
	public void addView(View child)
	{
		shop_gallery_relayout.addView(child);
	}
	
	private void printList(ArrayList<ImagePagerUri> mImageList)
	{
		String strtmp = "";
		for(ImagePagerUri mImagePagerUri: mImageList)
		{
			strtmp += mImagePagerUri.getImgUrl();
		}
		Log.d("mImageList", strtmp);
	}
	
	private void printListLocal(ArrayList<ImagePagerUri> mImageList)
	{
		String strtmp = "";
		for(ImagePagerUri mImagePagerUri: mImageList)
		{
			strtmp += mImagePagerUri.getLocalPath();
		}
		Log.d("mImageListLocal", strtmp);
	}
}
