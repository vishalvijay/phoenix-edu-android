package com.v4creations.phoenixedu.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class PhoenixEduImageLoader {
	private ImageLoader imageLoader;
	private Context context;
	String TAG = "PhoenixEduImageLoader";

	public PhoenixEduImageLoader(Context context) {
		this.context = context;
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this.context).defaultDisplayImageOptions(defaultOptions)
				.build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
	}

	public void assignImage(final ProgressBar progressBar,
			ImageView thumnailView, String yId) {
		String imageUrl = getImageUrl(yId);
		imageLoader.displayImage(imageUrl, thumnailView,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						((ImageView) view).setImageBitmap(null);
						progressBar.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						progressBar.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						progressBar.setVisibility(View.GONE);
						((ImageView) view).setImageBitmap(loadedImage);
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
					}
				});
	}

	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	private String getImageUrl(String yId) {
		String imageQuality = PhoenixEduUtil.getYoutubeVideoDiamention(context)
				.getQualityName();
		return String.format(PhoenixEduConstance.YOUTUBE_DEFAULT_IMAGE_URL,
				yId, imageQuality);
	}
}
