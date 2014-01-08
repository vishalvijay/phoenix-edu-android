package com.v4creations.phoenixedu.controller.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.v4creations.phoenixedu.R;
import com.v4creations.phoenixedu.controller.FavoriteYoutubeVideoLoader;
import com.v4creations.phoenixedu.controller.YoutubeVideoController;
import com.v4creations.phoenixedu.model.YoutubeVideo;
import com.v4creations.phoenixedu.util.PhoenixEduConstance;
import com.v4creations.phoenixedu.util.PhoenixEduImageLoader;

public class YoutubeVideoArrayAdapter extends ArrayAdapter<YoutubeVideo> {

	String TAG = "YoutubeVideoArrayAdapter";
	private PhoenixEduImageLoader phoenixEduImageLoader;
	private YoutubeVideoController youtubeVideoController;
	private LayoutInflater inflater;
	private ArrayList<YoutubeVideo> youtubeVideos;

	public YoutubeVideoArrayAdapter(
			YoutubeVideoController youtubeVideoController, Context context,
			int resource, ArrayList<YoutubeVideo> youtubeVideos) {
		super(context, resource, youtubeVideos);
		this.youtubeVideos = youtubeVideos;
		phoenixEduImageLoader = new PhoenixEduImageLoader(context);
		this.youtubeVideoController = youtubeVideoController;
		inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
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
		final YoutubeVideo youtubeVideo = getItem(position);
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.list_item_main_youtube_video, null);
			viewHolder = new ViewHolder();
			viewHolder.categoryTextView = (TextView) convertView
					.findViewById(R.id.categoryTextView);
			viewHolder.descriptionTextView = (TextView) convertView
					.findViewById(R.id.descriptionTextView);
			viewHolder.durationTextView = (TextView) convertView
					.findViewById(R.id.durationTextViews);
			viewHolder.newTextView = (TextView) convertView
					.findViewById(R.id.newTextView);
			viewHolder.thumbnailImageView = (ImageView) convertView
					.findViewById(R.id.thumbnailImageView);
			viewHolder.thumbnailLoadingProgressBar = (ProgressBar) convertView
					.findViewById(R.id.thumnailLoadingProgressBar);
			viewHolder.titleTextView = (TextView) convertView
					.findViewById(R.id.titleTextView);
			viewHolder.updatedAtTextView = (TextView) convertView
					.findViewById(R.id.updatedAtTextView);
			viewHolder.watchedImageView = (ImageView) convertView
					.findViewById(R.id.watchedImageView);
			viewHolder.favoriteImageView = (ImageButton) convertView
					.findViewById(R.id.favoriteImageButton);
			viewHolder.categoryTextView
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							String category = ((YoutubeVideo) v.getTag())
									.getCategory();
							youtubeVideoController.categoryFilter(category);
						}
					});
			viewHolder.favoriteImageView
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							toggleFavorite((ImageButton) v,
									(YoutubeVideo) v.getTag());
						}
					});
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		assineValues(viewHolder, youtubeVideo);
		return convertView;
	}

	protected void toggleFavorite(ImageButton v, YoutubeVideo youtubeVideo) {
		youtubeVideo.setFavorite(!youtubeVideo.isFavorite());
		assineFavorite(v, youtubeVideo.isFavorite());
		youtubeVideoController.toggleFavorite(youtubeVideo);
	}

	protected void toggleFavorite(ImageButton v) {

	}

	private void assineValues(ViewHolder viewHolder, YoutubeVideo youtubeVideo) {
		viewHolder.categoryTextView.setText("#"
				+ youtubeVideo.getCategory().toLowerCase(Locale.getDefault()));
		viewHolder.categoryTextView.setTag(youtubeVideo);
		viewHolder.descriptionTextView.setText(getDescription(youtubeVideo
				.getDescription()));
		viewHolder.durationTextView.setText(getDuration(youtubeVideo
				.getDuration()));
		viewHolder.newTextView
				.setVisibility(getVisibility(isNewVideo(youtubeVideo
						.getUpdatedAt())));
		viewHolder.titleTextView.setText(youtubeVideo.getTitle());
		viewHolder.updatedAtTextView
				.setText(getHumanReadableUpdateAt(youtubeVideo.getUpdatedAt()));
		viewHolder.watchedImageView.setVisibility(getVisibility(youtubeVideo
				.isWatched()));
		viewHolder.favoriteImageView.setTag(youtubeVideo);
		assineFavorite(viewHolder.favoriteImageView, youtubeVideo.isFavorite());
		phoenixEduImageLoader.assignImage(
				viewHolder.thumbnailLoadingProgressBar,
				viewHolder.thumbnailImageView, youtubeVideo.getYid());
	}

	private void assineFavorite(ImageButton favoriteImageView,
			boolean isFavorite) {

		if (isFavorite)
			favoriteImageView.setImageResource(R.drawable.ic_action_important);
		else
			favoriteImageView
					.setImageResource(R.drawable.ic_action_not_important);
	}

	private String getHumanReadableUpdateAt(String updatedAt) {
		String result;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					PhoenixEduConstance.HUMAN_READABLE_DATE_FORMATE,
					Locale.getDefault());
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			result = dateFormat.format(getCalender(updatedAt).getTime());
		} catch (ParseException e) {
			result = getContext().getString(R.string.unknown);
		}
		return result;
	}

	private int getVisibility(boolean isVisible) {
		return isVisible ? View.VISIBLE : View.GONE;
	}

	private boolean isNewVideo(String updatedAt) {
		boolean result;
		try {
			Calendar calendar = getCalender(updatedAt);
			Calendar nowCalendar = Calendar.getInstance();
			nowCalendar.add(Calendar.DAY_OF_YEAR, -2);
			result = calendar.after(nowCalendar);
		} catch (ParseException e) {
			result = false;
		}
		return result;
	}

	private Calendar getCalender(String updatedAt) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				PhoenixEduConstance.DEFAULT_DATE_FORMATE, Locale.getDefault());
		calendar.setTime(dateFormat.parse(updatedAt));
		return calendar;
	}

	private String getDuration(long duration) {
		Date date = new Date(duration * 1000);
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss",
				Locale.getDefault());
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(date);
	}

	private String getDescription(String description) {
		return description == null ? getContext().getString(R.string.unknown)
				: description;
	}

	public ImageLoader getImageLoader() {
		return phoenixEduImageLoader.getImageLoader();
	}

	public void getFavoriteYoutubeVideos(
			final FavoriteYoutubeVideoLoader favoriteYoutubeVideoLoader) {
		new AsyncTask<Void, Void, ArrayList<YoutubeVideo>>() {

			@Override
			protected ArrayList<YoutubeVideo> doInBackground(Void... arg0) {
				ArrayList<YoutubeVideo> result = new ArrayList<YoutubeVideo>();
				for (YoutubeVideo youtubeVideo : youtubeVideos) {
					if (youtubeVideo.isFavorite())
						result.add(youtubeVideo);
				}
				return result;
			}

			@Override
			protected void onPostExecute(ArrayList<YoutubeVideo> result) {
				favoriteYoutubeVideoLoader.onLoadingFinish(result);
			}
		}.execute();

	}

	private class ViewHolder {
		public TextView titleTextView, categoryTextView, durationTextView,
				updatedAtTextView, descriptionTextView, newTextView;
		public ImageView thumbnailImageView, watchedImageView;
		public ImageButton favoriteImageView;
		public ProgressBar thumbnailLoadingProgressBar;
	}
}
