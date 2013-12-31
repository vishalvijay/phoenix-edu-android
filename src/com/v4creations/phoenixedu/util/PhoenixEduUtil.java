package com.v4creations.phoenixedu.util;

import com.v4creations.phoenixedu.model.YoutubeVideoQualityDiamentions;

import android.content.Context;
import android.util.DisplayMetrics;

public class PhoenixEduUtil {
	public static YoutubeVideoQualityDiamentions getYoutubeVideoDiamention(
			Context context) {
		int density = context.getResources().getDisplayMetrics().densityDpi;
		switch (density) {
		case DisplayMetrics.DENSITY_LOW:
		case DisplayMetrics.DENSITY_MEDIUM:
			return PhoenixEduConstance.YOUTUBE_IMAGE_QUALITY_TYPES[0];
		case DisplayMetrics.DENSITY_HIGH:
			return PhoenixEduConstance.YOUTUBE_IMAGE_QUALITY_TYPES[1];
		case DisplayMetrics.DENSITY_XHIGH:
		case DisplayMetrics.DENSITY_XXHIGH:
			return PhoenixEduConstance.YOUTUBE_IMAGE_QUALITY_TYPES[2];
		case DisplayMetrics.DENSITY_XXXHIGH:
			return PhoenixEduConstance.YOUTUBE_IMAGE_QUALITY_TYPES[3];
		default:
			return PhoenixEduConstance.YOUTUBE_IMAGE_QUALITY_TYPES[1];
		}
	}
}
