package com.v4creations.phoenixedu.controller;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.v4creations.phoenixedu.model.OrmLiteCompactCursor;
import com.v4creations.phoenixedu.model.OrmLiteCompactCursor.OrmLiteCompactCursorLoader;
import com.v4creations.phoenixedu.model.YoutubeVideo;
import com.v4creations.phoenixedu.util.PhoenixEduConstance;
import com.v4creations.phoenixedu.util.PhoenixEduRestClient;
import com.v4creations.phoenixedu.util.db.YoutubeVideosRepository;
import com.v4creations.phoenixedu.view.fragments.YoutubeVideoListFragment;

public class YoutubeVideoController {
	private OrmLiteCompactCursor ormLiteCompactCursor;
	private YoutubeVideoSync youtubeVideoSync;
	private OrmLiteCompactCursorLoader ormLiteCompactCursorLoader;
	private YoutubeVideosRepository youtubeVideosRepository;
	protected String TAG = "YoutubeVideoController";
	private boolean isDestroyed = false;

	public YoutubeVideoController(Context context,
			YoutubeVideoListFragment youtubeVideoListFragment) {
		this.youtubeVideosRepository = new YoutubeVideosRepository(context);
		this.ormLiteCompactCursorLoader = (OrmLiteCompactCursorLoader) youtubeVideoListFragment;
		this.youtubeVideoSync = (YoutubeVideoSync) youtubeVideoListFragment;
	}

	private synchronized YoutubeVideosRepository getYoutubeVideosRepository() {
		return youtubeVideosRepository;
	}

	public void startSync() {
		if (isDestroyed)
			return;
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				return getYoutubeVideosRepository().getLastTime();
			}

			protected void onPostExecute(String result) {
				syncDeletedVideos(result, 0);
			};
		}.execute();
	}

	private void syncDeletedVideos(final String dateTimeString,
			final int errorCount) {
		if (isDestroyed)
			return;
		PhoenixEduRestClient.getDeletedVideos(dateTimeString,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray response) {
						super.onSuccess(response);
						YoutubeVideo[] youtubeVideos = new Gson().fromJson(
								response.toString(), YoutubeVideo[].class);
						asyncToDatabase(youtubeVideos, true);
						syncNewYoutubeVideos(dateTimeString, 1, 0);
						loadCursor();
					}

					@Override
					public void onFailure(Throwable e, JSONObject errorResponse) {
						super.onFailure(e, errorResponse);
						if (errorCount == 2) {
							youtubeVideoSync.onErrorYoutubeVideoSync(e
									.getMessage());
						} else
							syncDeletedVideos(dateTimeString, errorCount + 1);
					}

				});
	}

	private void syncNewYoutubeVideos(final String dateTimeString,
			final int page, final int errorCount) {
		if (isDestroyed)
			return;
		PhoenixEduRestClient.getYoutubeVideos(dateTimeString, page,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray response) {
						super.onSuccess(response);
						Log.e(TAG, dateTimeString);
						Log.e(TAG, response.toString());
						YoutubeVideo[] youtubeVideos = new Gson().fromJson(
								response.toString(), YoutubeVideo[].class);
						if (youtubeVideos.length < PhoenixEduConstance.PAGE_LIMIT)
							youtubeVideoSync.onFinishYoutubeVideoSync();
						else {
							asyncToDatabase(youtubeVideos, false);
							syncNewYoutubeVideos(dateTimeString, page + 1, 0);
							loadCursor();
						}
					}

					@Override
					public void onFailure(Throwable e, JSONObject errorResponse) {
						super.onFailure(e, errorResponse);
						if (errorCount == 2) {
							youtubeVideoSync.onErrorYoutubeVideoSync(e
									.getMessage());
						} else
							syncNewYoutubeVideos(dateTimeString, page,
									errorCount + 1);
					}

				});
	}

	private void asyncToDatabase(final YoutubeVideo[] youtubeVideos,
			final boolean isDeleting) {
		if (isDestroyed)
			return;

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {

				if (isDeleting)
					getYoutubeVideosRepository().deleteYoutubeVideoArray(
							youtubeVideos);
				else
					getYoutubeVideosRepository()
							.createOrUpdateYoutubeVideoArray(youtubeVideos);
				return null;
			}

		}.execute();
	}

	public void loadCursor() {
		if (isDestroyed)
			return;
		new AsyncTask<Void, Void, OrmLiteCompactCursor>() {

			@Override
			protected OrmLiteCompactCursor doInBackground(Void... params) {
				return getYoutubeVideosRepository().getAllYoutubeVideosCursor();
			}

			protected void onPostExecute(OrmLiteCompactCursor result) {
				if (ormLiteCompactCursor != null)
					ormLiteCompactCursor.close();
				ormLiteCompactCursor = result;
				ormLiteCompactCursorLoader
						.onFinishOrmLiteCompactCursorLoading(result.getCursor());
			};

		}.execute();
	}

	public void destroy() {
		isDestroyed = true;
		if (ormLiteCompactCursor != null)
			ormLiteCompactCursor.close();
		youtubeVideosRepository.close();
	}
}
