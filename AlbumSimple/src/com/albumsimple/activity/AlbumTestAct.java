package com.albumsimple.activity;

import java.io.File;
import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.albumsimple.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

public class AlbumTestAct extends BaseSelectPicAct implements OnClickListener {

	private ImageView iv_showresult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_albumtest);
		findViewById(R.id.tv_takephoto).setOnClickListener(this);
		findViewById(R.id.tv_selectphoto).setOnClickListener(this);
		findViewById(R.id.tv_selectandcrop).setOnClickListener(this);
		iv_showresult = (ImageView) findViewById(R.id.iv_showresult);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_takephoto:
			takePhoto();
			break;
		case R.id.tv_selectphoto:
			selectPhoto();
			break;
		case R.id.tv_selectandcrop:
			selectPhotoAndCrop();
			break;

		default:
			break;
		}
	}

	@Override
	protected void hasSelectPicsNoCrop(ArrayList<String> resultList) {
		// TODO Auto-generated method stub
		if (null != resultList) {
			if(resultList.size() > 0)
			{
				ImageLoader.getInstance().displayImage(Scheme.FILE.wrap(resultList.get(0)), iv_showresult);
			}
		}
	}

	@Override
	protected void hasTakePhotoPic(File mPicFile) {
		// TODO Auto-generated method stub
		if(null != mPicFile && mPicFile.exists())
		{
		ImageLoader.getInstance().displayImage(Scheme.FILE.wrap(mPicFile.getPath()), iv_showresult);
		}
	}

	@Override
	protected void hasPicsCroped(String mPicNativePath) {
		// TODO Auto-generated method stub
		if(null != mPicNativePath && new File(mPicNativePath).exists())
		{
		ImageLoader.getInstance().displayImage(Scheme.FILE.wrap(mPicNativePath), iv_showresult);
		}
	}

}
