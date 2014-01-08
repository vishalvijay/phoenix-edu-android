package com.v4creations.phoenixedu.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.v4creations.phoenixedu.model.YoutubeVideoQualityDiamentions;

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

	public static void hideSoftKeyBord(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static void showSoftKeyBord(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.SHOW_FORCED, 0);
	}

	public static boolean isEditTextEmpty(EditText editText) {
		return editText.getText().toString() == null
				|| editText.getText().toString().equals("");
	}
}
