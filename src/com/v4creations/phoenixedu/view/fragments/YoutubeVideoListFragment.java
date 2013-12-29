package com.v4creations.phoenixedu.view.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.v4creations.phoenixedu.R;
import com.v4creations.phoenixedu.controller.YoutubeVideoController;
import com.v4creations.phoenixedu.controller.YoutubeVideoSync;
import com.v4creations.phoenixedu.controller.adapter.YoutubeVideoCursorAdapter;
import com.v4creations.phoenixedu.model.OrmLiteCompactCursor.OrmLiteCompactCursorLoader;
import com.v4creations.phoenixedu.view.activitys.PhoenixEduMainFragmentActivity;

public class YoutubeVideoListFragment extends Fragment implements
		YoutubeVideoSync, OrmLiteCompactCursorLoader {
	private PhoenixEduMainFragmentActivity activity;
	private YoutubeVideoCursorAdapter adapter;
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
		initViews();
		controller.startSync();
	}

	private void initViews() {
		adapter = new YoutubeVideoCursorAdapter(activity,
				R.layout.list_item_main_youtube_video, null);
		gridView.setAdapter(adapter);
		gridView.setOnScrollListener(new PauseOnScrollListener(adapter
				.getImageLoader(), true, false));
		controller.loadCursor();
	}

	@Override
	public void onFinishOrmLiteCompactCursorLoading(Cursor cursor) {
		adapter.changeCursor(cursor);
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
	public void onFinishYoutubeVideoSync() {
		Toast.makeText(activity, "Sync finished", Toast.LENGTH_SHORT).show();
	}
}
