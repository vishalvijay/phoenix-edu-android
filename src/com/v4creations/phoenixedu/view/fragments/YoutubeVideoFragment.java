package com.v4creations.phoenixedu.view.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.v4creations.phoenixedu.R;
import com.v4creations.phoenixedu.controller.FavoriteYoutubeVideoLoader;
import com.v4creations.phoenixedu.controller.YoutubeVideoController;
import com.v4creations.phoenixedu.controller.YoutubeVideoSync;
import com.v4creations.phoenixedu.controller.adapter.YoutubeVideoArrayAdapter;
import com.v4creations.phoenixedu.model.YoutubeVideo;
import com.v4creations.phoenixedu.model.YoutubeVideo.YoutubeVideoFilter;
import com.v4creations.phoenixedu.view.activitys.PhoenixEduMainFragmentActivity;

public class YoutubeVideoFragment extends Fragment implements YoutubeVideoSync,
		FavoriteYoutubeVideoLoader, YoutubeVideoFilter {
	private PhoenixEduMainFragmentActivity activity;
	private YoutubeVideoArrayAdapter adapter;
	private YoutubeVideoController controller;
	private GridView gridView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = (PhoenixEduMainFragmentActivity) getActivity();
		return inflater.inflate(R.layout.fragment_youtube_video, null);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		gridView = (GridView) getView().findViewById(R.id.list);
		gridView.setEmptyView(getView().findViewById(android.R.id.empty));
		controller = new YoutubeVideoController(activity, this);
		controller.startSync();
		initViews();
	}

	private void initViews() {
		adapter = new YoutubeVideoArrayAdapter(controller, activity,
				R.layout.list_item_main_youtube_video,
				new ArrayList<YoutubeVideo>());
		gridView.setAdapter(adapter);
		gridView.setOnScrollListener(new PauseOnScrollListener(adapter
				.getImageLoader(), true, false));
		controller.loadYoutubeVideos();
	}

	@Override
	public void onErrorYoutubeVideoSync(String message) {
		Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		controller.destroy();
	}

	@Override
	public void onFinishYoutubeVideoSync(boolean isDataChanged) {
		if (isDataChanged)
			controller.loadYoutubeVideos();
		Toast.makeText(activity, "Sync finished", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onPageLoaded(ArrayList<YoutubeVideo> newYoutubeVideos) {
		adapter.addAll(newYoutubeVideos);
	}

	@Override
	public void onUpdateView(ArrayList<YoutubeVideo> youtubeVideos) {
		adapter.clear();
		adapter.addAll(youtubeVideos);
		adapter.getFavoriteYoutubeVideos(this);
	}

	@Override
	public void onLoadingFinish(ArrayList<YoutubeVideo> youtubeVideos) {
		((FavoriteYoutubeVideoLoader) activity).onLoadingFinish(youtubeVideos);
	}

	@Override
	public void onFavoriteItemToggle(YoutubeVideo youtubeVideo) {
		((FavoriteYoutubeVideoLoader) activity)
				.onFavoriteItemToggle(youtubeVideo);
	}

	public void searchFilter(CharSequence s) {
		adapter.getFilter().filter(s);
	}

	@Override
	public void categoryFilter(String category) {
		activity.categoryFilter(category);
	}
}
