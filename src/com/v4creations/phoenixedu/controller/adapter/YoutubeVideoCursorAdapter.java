package com.v4creations.phoenixedu.controller.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.v4creations.phoenixedu.R;
import com.v4creations.phoenixedu.model.YoutubeVideo;
import com.v4creations.phoenixedu.util.PhoenixEduConstance;
import com.v4creations.phoenixedu.util.PhoenixEduImageLoader;

public class YoutubeVideoCursorAdapter extends ResourceCursorAdapter {

	String TAG = "YoutubeVideoCursorAdapter";
	private PhoenixEduImageLoader phoenixEduImageLoader;

	public YoutubeVideoCursorAdapter(Context context, int layout, Cursor c) {
		super(context, layout, c, true);
		phoenixEduImageLoader = new PhoenixEduImageLoader(context);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder viewHolder;
		if (view.getTag() == null) {
			viewHolder = new ViewHolder();
			viewHolder.categoryTextView = (TextView) view
					.findViewById(R.id.categoryTextView);
			viewHolder.descriptionTextView = (TextView) view
					.findViewById(R.id.descriptionTextView);
			viewHolder.durationTextView = (TextView) view
					.findViewById(R.id.durationTextViews);
			viewHolder.newTextView = (TextView) view
					.findViewById(R.id.newTextView);
			viewHolder.thumbnailImageView = (ImageView) view
					.findViewById(R.id.thumbnailImageView);
			viewHolder.thumbnailLoadingProgressBar = (ProgressBar) view
					.findViewById(R.id.thumnailLoadingProgressBar);
			viewHolder.titleTextView = (TextView) view
					.findViewById(R.id.titleTextView);
			viewHolder.updatedAtTextView = (TextView) view
					.findViewById(R.id.updatedAtTextView);
			viewHolder.watchedImageView = (ImageView) view
					.findViewById(R.id.watchedImageView);
			view.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		assineValues(context, viewHolder);
	}

	private void assineValues(Context context, ViewHolder viewHolder) {
		viewHolder.categoryTextView.setText("#"
				+ getStringFromCursor(YoutubeVideo.COL_CATEGORY).toLowerCase(
						Locale.getDefault()));
		viewHolder.descriptionTextView.setText(getDescription(context));
		viewHolder.durationTextView.setText(getDuration());
		viewHolder.newTextView.setVisibility(getNewVideoVisibility());
		viewHolder.titleTextView
				.setText(getStringFromCursor(YoutubeVideo.COL_TITLE));
		viewHolder.updatedAtTextView.setText(getHumanReadableUpdateAt(context));
		viewHolder.watchedImageView.setVisibility(getIsWatched());
		phoenixEduImageLoader.assignImage(
				viewHolder.thumbnailLoadingProgressBar,
				viewHolder.thumbnailImageView,
				getStringFromCursor(YoutubeVideo.COL_YID));
	}

	private String getHumanReadableUpdateAt(Context context) {
		String result;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					PhoenixEduConstance.HUMAN_READABLE_DATE_FORMATE,
					Locale.getDefault());
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			result = dateFormat.format(getUpdatedAtCalender().getTime());
		} catch (ParseException e) {
			result = context.getString(R.string.unknown);
		}
		return result;
	}

	private int getIsWatched() {
		return getStringFromCursor(YoutubeVideo.COL_IS_WATCHED).equals("1") ? View.VISIBLE
				: View.GONE;
	}

	private String getDescription(Context context) {
		String description = getStringFromCursor(YoutubeVideo.COL_DESCRIPTION);
		return description == null ? context.getString(R.string.unknown)
				: description;
	}

	private int getNewVideoVisibility() {
		int result;
		try {
			Calendar calendar = getUpdatedAtCalender();
			Calendar nowCalendar = Calendar.getInstance();
			nowCalendar.add(Calendar.DAY_OF_YEAR, -2);
			result = calendar.after(nowCalendar) ? View.VISIBLE : View.GONE;
		} catch (ParseException e) {
			result = View.GONE;
		}
		return result;
	}

	private Calendar getUpdatedAtCalender() throws ParseException {
		String updatedAtString = getStringFromCursor(YoutubeVideo.COL_UPDATED_AT);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				PhoenixEduConstance.DEFAULT_DATE_FORMATE, Locale.getDefault());
		calendar.setTime(dateFormat.parse(updatedAtString));
		return calendar;
	}

	private String getDuration() {
		long duration = getCursor().getLong(
				getCursor().getColumnIndex(YoutubeVideo.COL_DURATION));
		Date date = new Date(duration * 1000);
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss",
				Locale.getDefault());
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(date);
	}

	private String getStringFromCursor(String column) {
		return getCursor().getString(getCursor().getColumnIndex(column));
	}

	public ImageLoader getImageLoader() {
		return phoenixEduImageLoader.getImageLoader();
	}

	private class ViewHolder {
		public TextView titleTextView, categoryTextView, durationTextView,
				updatedAtTextView, descriptionTextView, newTextView;
		public ImageView thumbnailImageView, watchedImageView;
		public ProgressBar thumbnailLoadingProgressBar;
	}
}
