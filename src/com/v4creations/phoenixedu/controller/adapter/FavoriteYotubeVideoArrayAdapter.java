package com.v4creations.phoenixedu.controller.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.v4creations.phoenixedu.R;
import com.v4creations.phoenixedu.model.YoutubeVideo;
import com.v4creations.phoenixedu.util.PhoenixEduImageLoader;

public class FavoriteYotubeVideoArrayAdapter extends ArrayAdapter<YoutubeVideo> {

	private LayoutInflater inflater;
	private PhoenixEduImageLoader phoenixEduImageLoader;

	public FavoriteYotubeVideoArrayAdapter(Context context, int resource,
			ArrayList<YoutubeVideo> youtubeVideos) {
		super(context, resource, youtubeVideos);
		phoenixEduImageLoader = new PhoenixEduImageLoader(context);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@SuppressLint("NewApi")
	@Override
	public void addAll(YoutubeVideo... items) {
		super.addAll(items);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			super.addAll(items);
		} else {
			for (YoutubeVideo element : items) {
				super.add(element);
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.favorite_list_item,null);
			viewHolder = new ViewHolder();
			viewHolder.thumbnailImageView = (ImageView) convertView
					.findViewById(R.id.thumbnailImageView);
			viewHolder.thumbnailLoadingProgressBar = (ProgressBar) convertView
					.findViewById(R.id.thumnailLoadingProgressBar);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		phoenixEduImageLoader.assignImage(
				viewHolder.thumbnailLoadingProgressBar,
				viewHolder.thumbnailImageView, getItem(position).getYid());
		return convertView;
	}

	public ImageLoader getImageLoader() {
		return phoenixEduImageLoader.getImageLoader();
	}

	private class ViewHolder {
		public ImageView thumbnailImageView;
		public ProgressBar thumbnailLoadingProgressBar;
	}
}
