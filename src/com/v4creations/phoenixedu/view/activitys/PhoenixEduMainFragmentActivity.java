package com.v4creations.phoenixedu.view.activitys;

import it.sephiroth.android.library.widget.HListView;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;

import com.v4creations.phoenixedu.R;
import com.v4creations.phoenixedu.controller.FavoriteYoutubeVideoLoader;
import com.v4creations.phoenixedu.controller.VerticalNavigationDrawer;
import com.v4creations.phoenixedu.controller.adapter.FavoriteYotubeVideoArrayAdapter;
import com.v4creations.phoenixedu.model.VerticalNavigationDrawerItem;
import com.v4creations.phoenixedu.model.VerticalNavigationDrawerItem.VerticalNavigationDrawerItemStatusListener;
import com.v4creations.phoenixedu.model.YoutubeVideo;
import com.v4creations.phoenixedu.model.YoutubeVideo.YoutubeVideoFilter;
import com.v4creations.phoenixedu.util.PhoenixEduUtil;
import com.v4creations.phoenixedu.view.fragments.YoutubeVideoFragment;

public class PhoenixEduMainFragmentActivity extends FragmentActivity implements
		FavoriteYoutubeVideoLoader, VerticalNavigationDrawerItemStatusListener,
		YoutubeVideoFilter {

	private VerticalNavigationDrawer verticalNavigationDrawer;
	private HListView favoriteHListView;
	private EditText searchEditText;
	private ImageButton searchActionBarImageButton;
	private ImageButton favoriteActionBarImageButton;
	private FavoriteYotubeVideoArrayAdapter favoriteArrayAdapter;
	private YoutubeVideoFragment youtubeVideoFragment;

	private static final String VND_KEY_SEARCH = "search",
			VND_KEY_FAVORITE = "favorites";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phoenix_edu_main);
		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState != null) {
				return;
			}
			youtubeVideoFragment = new YoutubeVideoFragment();
			youtubeVideoFragment.setArguments(getIntent().getExtras());
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, youtubeVideoFragment)
					.commit();
		}
		initViews();
	}

	private void initViews() {
		initFavoriteListView();
		searchEditText = (EditText) findViewById(R.id.searchEditText);
		searchEditText.addTextChangedListener(searchTextWatcher);
		searchActionBarImageButton = (ImageButton) findViewById(R.id.searchImageButton);
		favoriteActionBarImageButton = (ImageButton) findViewById(R.id.favoriteImageButton);
		initVerticalNavigationDrawer();
	}

	private void initVerticalNavigationDrawer() {
		verticalNavigationDrawer = new VerticalNavigationDrawer(this);
		verticalNavigationDrawer.add(VND_KEY_SEARCH,
				searchActionBarImageButton, searchEditText, false);
		verticalNavigationDrawer.add(VND_KEY_FAVORITE,
				favoriteActionBarImageButton,
				findViewById(R.id.favoriteVideoContainerRelativeLayout), false);
		verticalNavigationDrawer.setVNDIStatusChangeListener(this);
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

	@Override
	public void onFavoriteItemToggle(YoutubeVideo youtubeVideo) {
		if (youtubeVideo.isFavorite())
			favoriteArrayAdapter.add(youtubeVideo);
		else
			favoriteArrayAdapter.remove(youtubeVideo);
	}

	@Override
	public void onBackPressed() {
		if (verticalNavigationDrawer.isVisible())
			verticalNavigationDrawer.hideCurrent();
		else if (!PhoenixEduUtil.isEditTextEmpty(searchEditText))
			searchEditText.setText("");
		else
			super.onBackPressed();
	}

	private TextWatcher searchTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			youtubeVideoFragment.searchFilter(s);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	@Override
	public void onVNDIHide(String key, VerticalNavigationDrawerItem item) {
		if (key.equals(VND_KEY_SEARCH))
			PhoenixEduUtil.hideSoftKeyBord(getApplicationContext(),
					searchEditText);
	}

	@Override
	public void onVNDIShow(String key, VerticalNavigationDrawerItem item) {
		if (key.equals(VND_KEY_SEARCH)) {
			searchEditText.requestFocus();
			PhoenixEduUtil.showSoftKeyBord(getApplicationContext(),
					searchEditText);
		}

	}

	@Override
	public void categoryFilter(String category) {
		searchEditText.setText(category);
	}
}
