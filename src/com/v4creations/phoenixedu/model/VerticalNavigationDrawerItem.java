package com.v4creations.phoenixedu.model;

import android.view.View;

public class VerticalNavigationDrawerItem {
	private View mActionView;
	private View mView;

	public VerticalNavigationDrawerItem(View actionView, View view) {
		setActionView(actionView);
		setView(view);
	}

	public View getActionView() {
		return mActionView;
	}

	public void setActionView(View actionView) {
		this.mActionView = actionView;
	}

	public View getView() {
		return mView;
	}

	public void setView(View view) {
		this.mView = view;
	}

	public static interface VerticalNavigationDrawerItemStatusListener {
		public void onVNDIHide(String key, VerticalNavigationDrawerItem item);

		public void onVNDIShow(String key, VerticalNavigationDrawerItem item);
	}
}
