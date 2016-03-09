package com.albumsimple.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

/**
 * 
 * @description 详细描述：
 * @author samy
 * @date 2014-5-8 下午7:01:04
 */
public class ImageTools {
	public static final int UPLOAD_IMG_SIZE = 320 * 480 * 4;// 上传的图片最大尺寸
	public static final int SHOW_IMG_SIZE = 128 * 128;// 显示的图片最大尺寸
	public static final int CAPTURE_IMG_SIZE = 600;// 裁切大小

	public static final String TEMP_CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/image" + "/tmp";
	
	/**
	 * 判断是否有sd卡
	 * 
	 * @return
	 */
	public static boolean isSDCardExist() {
		return android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
	}

	/**
	 * 请求去拍照
	 */
	public static Intent getTakeCameraIntent(Uri photoUri) {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		return openCameraIntent;
	}

	/**
	 * 取得图片路径
	 */
	public static File initTempFile() {
		File uploadFileDir = new File(TEMP_CACHE_DIR);

		if (!uploadFileDir.exists()) {
			uploadFileDir.mkdirs();
			try {
				File nomedia = new File(uploadFileDir, ".nomedia");
				nomedia.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File picFile = new File(uploadFileDir, getPhotoFileName());
		if (!picFile.exists()) {
			try {
				picFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return picFile;
	}

	/**
	 * 用当前时间给取得的图片命名
	 */
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSS");
		return dateFormat.format(date) + ".jpg";
	}
	
}
