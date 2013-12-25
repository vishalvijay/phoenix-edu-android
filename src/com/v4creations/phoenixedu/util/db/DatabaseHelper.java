package com.v4creations.phoenixedu.util.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.v4creations.phoenixedu.model.YoutubeVideo;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "phoenix_edu.db";
	private static final int DATABASE_VERSION = 1;

	private Dao<YoutubeVideo, String> yotubeVideoDao = null;
	private RuntimeExceptionDao<YoutubeVideo, String> youtubeRuntimeExceptionDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, YoutubeVideo.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, YoutubeVideo.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Dao<YoutubeVideo, String> getYoutubeVideosDao() throws SQLException {
		if (yotubeVideoDao == null) {
			yotubeVideoDao = getDao(YoutubeVideo.class);
		}
		return yotubeVideoDao;
	}

	public RuntimeExceptionDao<YoutubeVideo, String> getYoutubeVideosDataDao() {
		if (youtubeRuntimeExceptionDao == null) {
			youtubeRuntimeExceptionDao = getRuntimeExceptionDao(YoutubeVideo.class);
		}
		return youtubeRuntimeExceptionDao;
	}

	@Override
	public void close() {
		super.close();
		yotubeVideoDao = null;
	}
}