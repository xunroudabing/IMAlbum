package com.opensource.cropImage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;

public class BitmapUtils {

	private static BitmapUtils mBitmapUtils = null;

	public static BitmapUtils getInstance() {
		if (mBitmapUtils == null) mBitmapUtils = new BitmapUtils();
		return mBitmapUtils;
	}

	public Bitmap resetBitmap(Bitmap source, Rect tarRect) {
		Bitmap target = Bitmap.createScaledBitmap(source, tarRect.width(), tarRect.height(), true);//(tarRect.width(), tarRect.height(), source.getConfig());
		
		return target;
	}

	// 通知刷新这块的图库
	public void refreshGallery(Context context, File file) {

		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri uri = Uri.fromFile(file);
		intent.setData(uri);
		context.sendBroadcast(intent);
	}
	
	
	public Bitmap zoomBitmap(String path) {
		Bitmap bitmap = null;
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, options);
			in.close();
			int i = 0;
			while (true) {
				if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000)) {
					in = new BufferedInputStream(new FileInputStream(new File(path)));
					options.inSampleSize = (int) Math.pow(2.0D, i);
					options.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeStream(in, null, options);
					break;
				}
				i += 1;
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
