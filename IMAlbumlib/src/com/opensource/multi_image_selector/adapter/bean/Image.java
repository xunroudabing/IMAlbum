package com.opensource.multi_image_selector.adapter.bean;

import com.opensource.TiTouchImageView.bean.ImagePagerUri;

/**
 * 图片实体
 * Created by Nereo on 2015/4/7.
 */
public class Image extends ImagePagerUri{
    public String path;
    public String thumbnailsPath;
    public String name;
    public long time;
    

    public Image(String path, String name, long time, String thumbnailsPath){
        this.path = path;
        this.name = name;
        this.time = time;
        this.thumbnailsPath = thumbnailsPath;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Image other = (Image) o;
            return this.path.equalsIgnoreCase(other.path);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }

	@Override
	public String getImgUrl() {
		// TODO Auto-generated method stub
		return cropFileSchem(path);
	}

	@Override
	public String getLocalPath() {
		// TODO Auto-generated method stub
		return path;
	}

	@Override
	public String getSmallImgUrl() {
		// TODO Auto-generated method stub
		return "";
	}
}
