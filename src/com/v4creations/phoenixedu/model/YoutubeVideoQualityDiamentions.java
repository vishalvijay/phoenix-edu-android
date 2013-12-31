package com.v4creations.phoenixedu.model;


public class YoutubeVideoQualityDiamentions {
	private String mQualityName;
	private int mWidth, mHeigth;

	public YoutubeVideoQualityDiamentions(String qualityName, int width,
			int heigth) {
		mQualityName = qualityName;
		mWidth = width;
		mHeigth = heigth;
	}

	public String getQualityName() {
		return mQualityName;
	}

	public int getWidht() {
		return mWidth;
	}

	public int getHeigth() {
		return mHeigth;
	}
}
