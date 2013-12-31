package com.v4creations.phoenixedu.controller;

import java.security.InvalidParameterException;
import java.util.HashMap;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.v4creations.phoenixedu.R;
import com.v4creations.phoenixedu.model.VerticalNavigationDrawerItem;

public class VerticalNavigationDrawer implements OnClickListener {
	private ViewGroup verticalNavigationDrawer;
	private HashMap<String, VerticalNavigationDrawerItem> items;
	private String currentlyShowingKey = null;

	public VerticalNavigationDrawer(Activity activity) {
		items = new HashMap<String, VerticalNavigationDrawerItem>();
		verticalNavigationDrawer = (ViewGroup) activity
				.findViewById(R.id.verticalNavigationDrawer);
		verticalNavigationDrawer.setVisibility(View.GONE);
	}

	public void add(String key, View actionView, View view, boolean addAsChaild) {
		if (key == null)
			throw new NullPointerException("key can't be null");
		if (key.trim().equals(""))
			throw new InvalidParameterException("key can't be empty");
		if (actionView == null)
			throw new NullPointerException("actionView can't be null");
		if (view == null)
			throw new NullPointerException("view can't be null");
		view.setVisibility(View.GONE);
		actionView.setTag(key);
		actionView.setOnClickListener(this);
		items.put(key, new VerticalNavigationDrawerItem(actionView, view));
		if (addAsChaild == true)
			verticalNavigationDrawer.addView(view);
	}

	@Override
	public void onClick(View actionView) {
		String key = (String) actionView.getTag();
		if (currentlyShowingKey == null) {
			showNew(key);
		} else if (currentlyShowingKey.equals(key)) {
			hideCurrent();
		} else {
			hideAndShowNew(key);
		}
	}

	private void hideAndShowNew(final String key) {
		final VerticalNavigationDrawerItem hideVerticalNavigationDrawerItem = items
				.get(currentlyShowingKey);
		hide(hideVerticalNavigationDrawerItem);
		items.get(key)
				.getActionView()
				.setBackgroundResource(
						R.drawable.action_bar_button_selected_selecter);
		Animation animation = new TranslateAnimation(0, 0, 0,
				-verticalNavigationDrawer.getHeight());
		animation.setDuration(300);
		animation.setFillAfter(true);

		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				hideVerticalNavigationDrawerItem.getView().setVisibility(
						View.GONE);
				verticalNavigationDrawer.setVisibility(View.GONE);
				showNew(key);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});

		verticalNavigationDrawer.startAnimation(animation);
	}

	private void hideCurrent() {
		VerticalNavigationDrawerItem item = items.get(currentlyShowingKey);
		hide(item);
		navigationAnimater(true, item.getView());
	}

	private void hide(VerticalNavigationDrawerItem item) {
		item.getActionView().setBackgroundResource(
				R.drawable.action_bar_button_selecter);
		currentlyShowingKey = null;
	}

	private void showNew(String key) {
		VerticalNavigationDrawerItem item = items.get(key);
		show(item, key);
		navigationAnimater(false, null);
	}

	private void show(VerticalNavigationDrawerItem item, String key) {
		item.getActionView().setBackgroundResource(
				R.drawable.action_bar_button_selected_selecter);
		item.getView().setVisibility(View.VISIBLE);
		currentlyShowingKey = key;
	}

	private void navigationAnimater(final boolean isHide, final View view) {
		Animation animation;
		if (isHide)
			animation = new TranslateAnimation(0, 0, 0,
					-verticalNavigationDrawer.getHeight());
		else
			animation = new TranslateAnimation(0, 0,
					-verticalNavigationDrawer.getHeight(), 0);
		animation.setDuration(300);
		animation.setFillAfter(true);

		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				if (isHide == false)
					verticalNavigationDrawer.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (isHide == true) {
					verticalNavigationDrawer.setVisibility(View.GONE);
					view.setVisibility(View.GONE);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});

		verticalNavigationDrawer.startAnimation(animation);
	}
}
