package com.v4creations.phoenixedu.util.db;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import android.content.Context;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.v4creations.phoenixedu.model.OrmLiteCompactCursor;
import com.v4creations.phoenixedu.model.YoutubeVideo;
import com.v4creations.phoenixedu.util.PhoenixEduConstance;

public class YoutubeVideosRepository {

	private DatabaseHelper db;
	private Dao<YoutubeVideo, String> youtubeVideoDao;
	private DatabaseManager dbManager;

	public YoutubeVideosRepository(Context context) {
		try {
			dbManager = new DatabaseManager();
			db = dbManager.getHelper(context);
			youtubeVideoDao = db.getYoutubeVideosDao();
		} catch (SQLException e) {
		}
	}

	public void createOrUpdateYoutubeVideoArray(
			final YoutubeVideo[] youtubeVideos) {
		try {
			youtubeVideoDao.callBatchTasks(new Callable<YoutubeVideo>() {
				@Override
				public YoutubeVideo call() throws Exception {

					for (YoutubeVideo youtubeVideo : youtubeVideos) {
						youtubeVideoDao.createOrUpdate(youtubeVideo);
					}
					return null;
				}

			});
		} catch (Exception e) {
		}
	}

	public void deleteYoutubeVideoArray(final YoutubeVideo[] youtubeVideos) {
		try {
			youtubeVideoDao.callBatchTasks(new Callable<YoutubeVideo>() {
				@Override
				public YoutubeVideo call() throws Exception {

					for (YoutubeVideo youtubeVideo : youtubeVideos) {
						youtubeVideoDao.delete(youtubeVideo);
					}
					return null;
				}

			});
		} catch (Exception e) {
		}
	}

	public OrmLiteCompactCursor getAllYoutubeVideosCursor() {
		OrmLiteCompactCursor result = null;
		QueryBuilder<YoutubeVideo, String> qb = youtubeVideoDao.queryBuilder();
		qb.orderBy(YoutubeVideo.COL_UPDATED_AT, false);
		try {
			CloseableIterator<YoutubeVideo> iterator = youtubeVideoDao
					.iterator(qb.prepare());
			AndroidDatabaseResults results = (AndroidDatabaseResults) iterator
					.getRawResults();
			result = new OrmLiteCompactCursor(results.getRawCursor(), iterator);
		} catch (SQLException e) {
		}
		return result;
	}

	public String getLastTime() {
		String result;
		QueryBuilder<YoutubeVideo, String> qb = youtubeVideoDao.queryBuilder();
		qb.orderBy(YoutubeVideo.COL_UPDATED_AT, false);
		try {
			YoutubeVideo youtubeVideo = qb.queryForFirst();
			if (youtubeVideo != null)
				result = youtubeVideo.getUpdatedAt();
			else
				result = PhoenixEduConstance.DEFAULT_DATE_TIME;
		} catch (SQLException e) {
			result = PhoenixEduConstance.DEFAULT_DATE_TIME;
		}
		return result;
	}

	public void close() {
		dbManager.releaseHelper(db);
	}
}