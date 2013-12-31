package com.v4creations.phoenixedu.controller;

import java.util.ArrayList;

import com.v4creations.phoenixedu.model.YoutubeVideo;

public interface YoutubeVideoSync {
	public void onFinishYoutubeVideoSync(boolean isDataChanged);

	public void onPageLoaded(ArrayList<YoutubeVideo> newYoutubeVideos);

	public void onErrorYoutubeVideoSync(String message);

	public void onUpdateView(ArrayList<YoutubeVideo> youtubeVideos);
}
