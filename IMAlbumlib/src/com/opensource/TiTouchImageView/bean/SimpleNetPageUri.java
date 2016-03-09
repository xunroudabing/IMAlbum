package com.opensource.TiTouchImageView.bean;


/**
 * 单张网络图片的查看bean类
 * @description：
 * @author zhangjianlin (990996641)
 * @date 2015年6月13日 上午10:16:35
 */
public class SimpleNetPageUri extends ImagePagerUri{

	private static final long serialVersionUID = 1154408727883217192L;
	private String netUri;
	
	
	
	public String getNetUri() {
		return netUri;
	}

	public void setNetUri(String netUri) {
		this.netUri = netUri;
	}

	@Override
	public String getImgUrl() {
		// TODO Auto-generated method stub
		return netUri;
	}

	@Override
	public String getLocalPath() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getSmallImgUrl() {
		// TODO Auto-generated method stub
		return "";
	}

}
