package com.v4creations.phoenixedu.controller;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.v4creations.phoenixedu.model.YoutubeVideo;
import com.v4creations.phoenixedu.model.YoutubeVideo.YoutubeVideoFilter;
import com.v4creations.phoenixedu.util.PhoenixEduConstance;
import com.v4creations.phoenixedu.util.PhoenixEduRestClient;
import com.v4creations.phoenixedu.util.db.YoutubeVideosRepository;
import com.v4creations.phoenixedu.view.fragments.YoutubeVideoFragment;

public class YoutubeVideoController implements YoutubeVideoFilter {
	protected String TAG = "YoutubeVideoController";
	private YoutubeVideoSync youtubeVideoSync;
	private YoutubeVideosRepository youtubeVideosRepository;
	private FavoriteYoutubeVideoLoader favoriteYoutubeVideoLoader;
	private YoutubeVideoFilter youtubeVideoFilter;
	private boolean isDestroyed = false;
	private boolean isDataChanged = false;

	public YoutubeVideoController(Context context,
			YoutubeVideoFragment youtubeVideoListFragment) {
		this.youtubeVideosRepository = new YoutubeVideosRepository(context);
		youtubeVideoSync = (YoutubeVideoSync) youtubeVideoListFragment;
		favoriteYoutubeVideoLoader = (FavoriteYoutubeVideoLoader) youtubeVideoListFragment;
		youtubeVideoFilter = (YoutubeVideoFilter) youtubeVideoListFragment;
	}

	public void startSync() {
		if (isDestroyed)
			return;
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				return youtubeVideosRepository.getLastTime();
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
						if (youtubeVideos.length > 0) {
							isDataChanged = true;
							asyncToDatabase(youtubeVideos, true);
						}
						syncNewYoutubeVideos(dateTimeString, 1, 0);
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
						YoutubeVideo[] youtubeVideos = new Gson().fromJson(
								response.toString(), YoutubeVideo[].class);
						asyncToDatabase(youtubeVideos, false);
						if (youtubeVideos.length > 0)
							isDataChanged = true;
						if (youtubeVideos.length < PhoenixEduConstance.PAGE_LIMIT)
							youtubeVideoSync
									.onFinishYoutubeVideoSync(isDataChanged);
						else {
							youtubeVideoSync
									.onPageLoaded(new ArrayList<YoutubeVideo>(
											Arrays.asList(youtubeVideos)));
							syncNewYoutubeVideos(dateTimeString, page + 1, 0);
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
					youtubeVideosRepository
							.deleteYoutubeVideoArray(youtubeVideos);
				else
					youtubeVideosRepository
							.createOrUpdateYoutubeVideoArray(youtubeVideos);
				return null;
			}

		}.execute();
	}

	public void loadYoutubeVideos() {
		if (isDestroyed)
			return;
		new AsyncTask<Void, Void, ArrayList<YoutubeVideo>>() {

			@Override
			protected ArrayList<YoutubeVideo> doInBackground(Void... params) {
				return youtubeVideosRepository.getAllYoutubeVideos();
			}

			protected void onPostExecute(ArrayList<YoutubeVideo> result) {
				youtubeVideoSync.onUpdateView(result);
			};

		}.execute();
	}

	public void destroy() {
		isDestroyed = true;
		youtubeVideosRepository.close();
	}

	public void toggleFavorite(final YoutubeVideo youtubeVideo) {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				youtubeVideosRepository.toggleFavorite(youtubeVideo);
				return null;
			}

		}.execute();
		favoriteYoutubeVideoLoader.onFavoriteItemToggle(youtubeVideo);
	}

	@Override
	public void categoryFilter(String category) {
		youtubeVideoFilter.categoryFilter(category);
	}
}
