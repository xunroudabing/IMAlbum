package com.opensource.TiTouchImageView;

import com.opensource.TiTouchImageView.bean.ImagePagerUri;

/***  本地图片数据*/
public class LocateImagePagerUrl extends ImagePagerUri{

	private static final long serialVersionUID = -5909931175608057100L;
	/** 
	 * @description：
	 * @author zhangjianlin (990996641)
	 * @date 2015年5月5日 下午1:55:05
	 */
	private String locateUrl;
	
	
	public String getLocateUrl() {
		return locateUrl;
	}

	public void setLocateUrl(String locatePath) {
		this.locateUrl = locatePath;
	}

	@Override
	public String getImgUrl() {
		// TODO Auto-generated method stub
		return cropFileSchem(locateUrl);
	}

	@Override
	public String getLocalPath() {
		// TODO Auto-generated method stub
		return locateUrl;
	}

	@Override
	public String getSmallImgUrl() {
		// TODO Auto-generated method stub
		return "";
	}

}
