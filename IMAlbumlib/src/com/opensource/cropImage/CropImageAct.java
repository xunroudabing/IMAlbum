package com.opensource.cropImage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.imalbumlib.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.opensource.configure.imgload.GlobalImageLoader;
import com.opensource.utils.FileUtils;
/**
 * @Description:截图的activity
 * @author zhangjianlin
 * @date 2015-8-19 下午4:40:02
 */
public class CropImageAct extends Activity {

	private TextView left, title, right;
	private CropTouchImageView cropimage;// 截图的ImageView
	RelativeLayout rl_title;

	public static final String SCALEW = "scaleW";
	public static final String SCALEH = "SCALEH";
	public static final String LIMITW = "limitWidth";
	public static final String LIMITH = "limitHight";

	private int scaleW = 200, scaleH = 200, limitWidth = 600, limitHight = 600;// 截图区域的宽高

	public static final String RESIZEBIT = "RESIZEBIT";
	private boolean isResizeBitmat = true;// 是否安截图区域截取等宽高的图片

	public static final String PHOTOURI = "photoUri";
	private String photoOrgUri = null;// 原始图片地址

	public static final String RESULT = "resultPath";
	private String photoResultPath;// 图片结果地址

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_croplayout);
		initView();
	}

	void initView() {
		left = (TextView) findViewById(R.id.left);
		left.setOnClickListener(leftOnClickListener);
		title = (TextView) findViewById(R.id.title);
		right = (TextView) findViewById(R.id.right);
		right.setOnClickListener(leftOnClickListener);
		rl_title = (RelativeLayout) findViewById(R.id.rl_title);
		cropimage = (CropTouchImageView) findViewById(R.id.cropimage);
		scaleW = getIntent().getIntExtra(SCALEW, 200);
		scaleH = getIntent().getIntExtra(SCALEH, 200);
		scaleW = (int) dpToPx(scaleW);
		scaleH = (int) dpToPx(scaleH);
		limitWidth = getIntent().getIntExtra(LIMITW, 600);
		limitHight = getIntent().getIntExtra(LIMITH, 600);
		isResizeBitmat = getIntent().getBooleanExtra(RESIZEBIT, true);

		photoResultPath = getIntent().getStringExtra(RESULT);
		photoOrgUri = getIntent().getStringExtra(PHOTOURI);

		if (scaleW > getResources().getDisplayMetrics().widthPixels || scaleW == 0)
			scaleW = getResources().getDisplayMetrics().widthPixels;
		if (scaleH > getResources().getDisplayMetrics().heightPixels || scaleH == 0)
			scaleH = getResources().getDisplayMetrics().heightPixels;
		String tmpStr = "file://";
		if(!TextUtils.isEmpty(photoOrgUri) && photoOrgUri.startsWith(tmpStr))
		{
			cropimage.setImageBitmap(BitmapUtils.getInstance().zoomBitmap(photoOrgUri.substring(tmpStr.length())));
		}else{
			ImageLoader.getInstance().getDiscCache().remove(photoOrgUri);
			ImageLoader.getInstance().getMemoryCache().remove(photoOrgUri);
			GlobalImageLoader.getIntance().displayImg(cropimage, photoOrgUri, new CropImageLoaderListener());
		}
		cropimage.setCropWH(scaleW, scaleH);
	}

	OnClickListener leftOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.left) {
				setResult(RESULT_CANCELED);
				finish();
			} else if (id == R.id.right) {
				if (!cropimage.isBitmapNull()) {
					saveImageBitmap();
					Intent data = new Intent();
					data.putExtra(RESULT, photoResultPath);
					setResult(RESULT_OK, data);
				}else{
					Toast.makeText(CropImageAct.this, getResources().getString(R.string.album_crop_null_error), Toast.LENGTH_LONG).show();
				}
				finish();
			}
		}
	};

	boolean saveImageBitmap() {
		Bitmap mBitmap = cropimage.getCropBitmap(isResizeBitmat, limitWidth, limitHight);
		return FileUtils.bitmapToFile(mBitmap, photoResultPath);
	}

	/** dp 转换 */
	private float dpToPx(float dp) {
		return dp * this.getResources().getDisplayMetrics().density;
	}
}
