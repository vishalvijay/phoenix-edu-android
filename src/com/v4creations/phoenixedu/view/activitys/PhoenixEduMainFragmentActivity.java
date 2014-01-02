package com.v4creations.phoenixedu.view.activitys;

import it.sephiroth.android.library.widget.HListView;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;
import android.widget.ImageButton;

import com.v4creations.phoenixedu.R;
import com.v4creations.phoenixedu.controller.FavoriteYoutubeVideoLoader;
import com.v4creations.phoenixedu.controller.VerticalNavigationDrawer;
import com.v4creations.phoenixedu.controller.adapter.FavoriteYotubeVideoArrayAdapter;
import com.v4creations.phoenixedu.model.YoutubeVideo;

public class PhoenixEduMainFragmentActivity extends FragmentActivity implements
		FavoriteYoutubeVideoLoader {

	private VerticalNavigationDrawer verticalNavigationDrawer;
	private HListView favoriteHListView;
	private EditText searchEditText;
	private ImageButton searchActionBarImageButton;
	private ImageButton favoriteActionBarImageButton;
	private FavoriteYotubeVideoArrayAdapter favoriteArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phoenix_edu_main);
		initViews();
	}

	private void initViews() {
		initFavoriteListView();
		searchEditText = (EditText) findViewById(R.id.searchEditText);
		searchActionBarImageButton = (ImageButton) findViewById(R.id.searchImageButton);
		favoriteActionBarImageButton = (ImageButton) findViewById(R.id.favoriteImageButton);
		initVerticalNavigationDrawer();
	}

	private void initVerticalNavigationDrawer() {
		verticalNavigationDrawer = new VerticalNavigationDrawer(this);
		verticalNavigationDrawer.add("search", searchActionBarImageButton,
				searchEditText, false);
		verticalNavigationDrawer.add("favorites", favoriteActionBarImageButton,
				findViewById(R.id.favoriteVideoContainerRelativeLayout), false);
	}

	private void initFavoriteListView() {
		favoriteArrayAdapter = new FavoriteYotubeVideoArrayAdapter(
				getApplicationContext(), R.layout.favorite_list_item,
				new ArrayList<YoutubeVideo>());
		favoriteHListView = (HListView) findViewById(R.id.favoriteHList);
		favoriteHListView.setLongClickable(false);
		favoriteHListView.setEmptyView(findViewById(R.id.emptyTextView));
		favoriteHListView.setAdapter(favoriteArrayAdapter);
	}

	@Override
	public void onLoadingFinish(ArrayList<YoutubeVideo> youtubeVideos) {
		favoriteArrayAdapter.clear();
		favoriteArrayAdapter.addAll(youtubeVideos);
	}
}
