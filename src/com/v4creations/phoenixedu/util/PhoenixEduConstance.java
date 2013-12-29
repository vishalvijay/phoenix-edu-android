package com.v4creations.phoenixedu.util;

import com.v4creations.phoenixedu.model.YoutubeVideoQualityDiamentions;

public class PhoenixEduConstance {
	public static final String DEFAULT_DATE_TIME = "2012-12-21T20:19:55.707Z";
	public static final String YOUTUBE_DEFAULT_IMAGE_URL = "http://i1.ytimg.com/vi/%1$s/%2$s.jpg";
	public static final String DEFAULT_FONT = "fonts/Roboto-Light.ttf";
	public static final String DEFAULT_DATE_FORMATE = "yyyy-MM-dd'T'HH:mm:ss";
	public static final String HUMAN_READABLE_DATE_FORMATE = "d MMM yyyy h:mm a";
	public static final int PAGE_LIMIT = 100;

	public static final YoutubeVideoQualityDiamentions[] YOUTUBE_IMAGE_QUALITY_TYPES = {
			getQualityDiamention("default", 120, 90),
			getQualityDiamention("mqdefault", 320, 180),
			getQualityDiamention("hqdefault", 480, 360),
			getQualityDiamention("sddefault", 640, 480) };

	private static YoutubeVideoQualityDiamentions getQualityDiamention(
			String qualityName, int width, int height) {
		return new YoutubeVideoQualityDiamentions(qualityName, width, height);
	}
}
