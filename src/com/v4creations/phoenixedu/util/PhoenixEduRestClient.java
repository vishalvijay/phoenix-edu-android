package com.v4creations.phoenixedu.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class PhoenixEduRestClient {
	private static final String BASE_URL = "http://phoenixedu.herokuapp.com/";
	private static final String VIDEO_URL = "youtube_videos";
	private static final String DELETED_VIDEO_URL = "deleted_youtube_videos";
	private static final String PARAM_TIME = "time";
	private static final String PARAM_PAGE = "page";

	private static AsyncHttpClient client = new AsyncHttpClient();
	protected String TAG="PhoenixEduRestClient";

	public static void getYoutubeVideos(String dateTimeString, Integer page,
			JsonHttpResponseHandler jsonHttpResponseHandler) {
		client.get(getAbsoluteUrl(VIDEO_URL),
				getRequestParams(dateTimeString, page), jsonHttpResponseHandler);
	}

	public static void getDeletedVideos(String dateTimeString,
			JsonHttpResponseHandler jsonHttpResponseHandler) {
		client.get(getAbsoluteUrl(DELETED_VIDEO_URL),
				getRequestParams(dateTimeString, null), jsonHttpResponseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

	private static RequestParams getRequestParams(String dateTimeString,
			Integer page) {
		RequestParams params = new RequestParams();
		if (dateTimeString != null)
			params.put(PARAM_TIME, dateTimeString);
		if (page != null)
			params.put(PARAM_PAGE, page.toString());
		return params;
	}

}
