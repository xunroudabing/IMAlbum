package com.opensource.TiTouchImageView.bean;

import java.io.Serializable;
import android.text.TextUtils;

/**
 * 图片翻页的数据基础类
 * @author zhangjianlin
 *
 */
public abstract class ImagePagerUri implements Serializable{
	

	/**
	 * 网络图片地址
	 * @return
	 */
	public abstract String getImgUrl();
	
	/**
	 * 本地图片地址;没有填空
	 * @return
	 */
	public abstract String getLocalPath();

	/**
	 * 图片的缩略图
	 * @return
	 */
	public abstract String getSmallImgUrl();
	
	public String cropFileSchem(String pathFile)
	{
		if(TextUtils.isEmpty(pathFile))return "";
		return "file://" + pathFile;
	}
	
}
