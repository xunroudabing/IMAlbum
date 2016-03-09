package com.albumsimple.activity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.os.Environment;

/**
 * 文件操作工具类
 * 
 * @author fxl time: 2012-9-24 下午4:31:11
 */
public class FilecacheTool {
	/** 默认的文件保存路径 */
	private static final String SDCARD_PATH = Environment.getExternalStorageState().equalsIgnoreCase(
			Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";
	private static final String defaultFolderName = SDCARD_PATH + "/changwang/savePic";

	public static String getDefaultfoldername() {  
		return defaultFolderName;
	}

	/**
	 * 从网络上获取图片
	 * 
	 * @param imageUrl
	 *            需获取图片的url地址
	 * @return byte[]
	 * @throws Exception
	 */
	public byte[] getImage(String imageUrl) throws Exception {
		InputStream inStream = getImageStream(imageUrl);
		if (inStream != null) { return readStream(inStream); }
		return null;
	}

	/**
	 * 从网络上获取图片
	 * 
	 * @param path
	 *            需获取图片的url地址
	 * @return InputStream
	 * @throws Exception
	 */
	public InputStream getImageStream(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) { return conn.getInputStream(); }
		return null;
	}

	/**
	 * 把输入流转成字节数组
	 * 
	 * @param inStream
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	/**
	 * 
	 * 方法概述：判断文件夹是否存在,如果不存在则创建文件夹
	 * 
	 * @description 方法详细描述：
	 * @author 刘成伟（wwwlllll@126.com）
	 * @param @param path
	 * @return void
	 * @throws
	 * @Title: FileTool.java
	 * @Package com.huika.huixin.utils
	 * @date 2014-3-21 下午6:29:33
	 */
	public static void isExist(String path) {
		File file = new File(path);
		// 判断文件夹是否存在,如果不存在则创建文件夹
		if (!file.exists()) {
			file.mkdirs();
			try {
				File nomedia = new File(file, ".nomedia");
				nomedia.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



	/**
	 * 获取文件或文件夹大小
	 */
	public static long getFileSize(File file) {
		long size = 0;
		if (file.isDirectory()) {
			for (File tempFile : file.listFiles()) {
				size += getFileSize(tempFile);
			}
		} else {
			size += file.length();
		}
		return size;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 * @throws Exception
	 */
	public static void copyFile(String oldPath, String newPath) throws Exception {
		int byteread = 0;
		File oldfile = new File(oldPath);
		if (oldfile.exists()) { // 文件存在时
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				FileInputStream fis = new FileInputStream(oldPath); // 读入原文件
				bis = new BufferedInputStream(fis);
				FileOutputStream fos = new FileOutputStream(newPath);
				bos = new BufferedOutputStream(fos);
				byte[] buffer = new byte[1024];
				while ((byteread = bis.read(buffer)) != -1) {
					bos.write(buffer, 0, byteread);
				}
			} finally {
				if (bis != null) {
					bis.close();
				}
				if (bos != null) {
					bos.close();
				}
			}
		}

	}

	/**
	 * 删除文件或文件夹
	 * 
	 * @param file
	 */
	public static void clear(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				for (File f : file.listFiles()) {
					clear(f);
					file.delete();
				}
			} else {
				file.delete();
			}
		}
	}

	/**
	 * 保存文件
	 * 
	 * @param bm
	 * @param fileName
	 * @param path
	 *            相对SD卡保存路径，如为null或""字符串将默认为 "/EacanNews"
	 * @throws IOException
	 */
	public static void saveFile(Bitmap bm, String fileName, String path) throws IOException {
		String subForder = FilecacheTool.getDefaultfoldername() + path;
		File foder = new File(subForder);
		if (!foder.exists()) {
			foder.mkdirs();
		}
		File myCaptureFile = new File(subForder, fileName);
		if (!myCaptureFile.exists()) {
			myCaptureFile.createNewFile();
		}
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}
}
