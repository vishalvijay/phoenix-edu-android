package com.v4creations.phoenixedu.model;

import android.database.Cursor;

import com.j256.ormlite.dao.CloseableIterator;

public class OrmLiteCompactCursor {
	private Cursor mCursor;
	private CloseableIterator<YoutubeVideo> mCloseableIterator;

	public OrmLiteCompactCursor() {
	}

	public OrmLiteCompactCursor(Cursor cursor,
			CloseableIterator<YoutubeVideo> closeableIterator) {
		setCursor(cursor);
		setCloseableIterator(closeableIterator);
	}

	public Cursor getCursor() {
		return mCursor;
	}

	public void setCursor(Cursor cursor) {
		this.mCursor = cursor;
	}

	public CloseableIterator<YoutubeVideo> getCloseableIterator() {
		return mCloseableIterator;
	}

	public void setCloseableIterator(
			CloseableIterator<YoutubeVideo> closeableIterator) {
		this.mCloseableIterator = closeableIterator;
	}

	public static interface OrmLiteCompactCursorLoader {
		public void onFinishOrmLiteCompactCursorLoading(Cursor cursor);
	}

	public void close() {
		getCloseableIterator().closeQuietly();
	}

}
