package com.albumsimple.activity;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import com.opensource.TiTouchImageView.bean.ImagePagerUri;
import com.opensource.TiTouchImageView.bean.SimpleNetPageUri;
import com.opensource.configure.IntentUtils;

public class ImageTool extends IntentUtils {

	/**
	 * 请求去拍照 
	 */
	public static Intent getTakeCameraIntent(Uri photoUri) {  
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		return openCameraIntent;
	}	
	
}
