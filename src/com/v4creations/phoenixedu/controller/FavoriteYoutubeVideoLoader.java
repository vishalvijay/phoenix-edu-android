package com.v4creations.phoenixedu.controller;

import java.util.ArrayList;

import com.v4creations.phoenixedu.model.YoutubeVideo;

public interface FavoriteYoutubeVideoLoader {
	public void onLoadingFinish(ArrayList<YoutubeVideo> youtubeVideos);

	public void onFavoriteItemToggle(YoutubeVideo youtubeVideo);
}
