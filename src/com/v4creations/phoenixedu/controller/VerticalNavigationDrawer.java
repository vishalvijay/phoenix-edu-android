package com.v4creations.phoenixedu.controller;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

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
	private String currentlyShowingKey = null, backUpKey = null;
	private final static int DEFAULT_VND_RESOURCE_ID = R.id.verticalNavigationDrawer;

	public VerticalNavigationDrawer(Activity activity) {
		verticalNavigationDrawer = (ViewGroup) activity
				.findViewById(DEFAULT_VND_RESOURCE_ID);
		init();
	}

	public VerticalNavigationDrawer(Activity activity, int VNDResourceId) {
		verticalNavigationDrawer = (ViewGroup) activity
				.findViewById(VNDResourceId);
		init();
	}

	private void init() {
		items = new HashMap<String, VerticalNavigationDrawerItem>();
		verticalNavigationDrawer.setVisibility(View.GONE);
	}

	public void add(String key, View actionView, View view,
			boolean isInflatedView) {
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
		if (isInflatedView == true)
			verticalNavigationDrawer.addView(view);
	}

	@Override
	public void onClick(View actionView) {
		if (backUpKey != null) {
			items.get(backUpKey)
					.getActionView()
					.setBackgroundResource(
							R.drawable.action_bar_button_selecter);
			backUpKey = null;
		}
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
		VerticalNavigationDrawerItem hideVerticalNavigationDrawerItem = items
				.get(currentlyShowingKey);
		backUpKey = key;
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
				verticalNavigationDrawer.setVisibility(View.GONE);
				showNew(key);
				backUpKey = null;
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
		navigationAnimater(true);
	}

	private void hide(VerticalNavigationDrawerItem item) {
		item.getActionView().setBackgroundResource(
				R.drawable.action_bar_button_selecter);
		currentlyShowingKey = null;
	}

	private void showNew(String key) {
		for (Map.Entry<String, VerticalNavigationDrawerItem> entry : items
				.entrySet()) {
			entry.getValue().getView().setVisibility(View.GONE);
		}
		VerticalNavigationDrawerItem item = items.get(key);
		show(item, key);
		navigationAnimater(false);
	}

	private void show(VerticalNavigationDrawerItem item, String key) {
		item.getActionView().setBackgroundResource(
				R.drawable.action_bar_button_selected_selecter);
		item.getView().setVisibility(View.VISIBLE);
		currentlyShowingKey = key;
	}

	private void navigationAnimater(final boolean isHide) {
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
				if (isHide == true)
					verticalNavigationDrawer.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});

		verticalNavigationDrawer.startAnimation(animation);
	}
}
