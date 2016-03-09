package com.albumsimple.activity;

import android.app.Application;

public class BaseApp extends Application{
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		ImgCacheUtil.getInstance(this);
	}
}
