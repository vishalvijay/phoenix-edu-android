package com.v4creations.phoenixedu.view.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.v4creations.phoenixedu.util.PhoenixEduConstance;

public class CustomeTextView extends TextView {

	public CustomeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CustomeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomeTextView(Context context) {
		super(context);
		init();
	}

	public void init() {
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
				PhoenixEduConstance.DEFAULT_FONT);
		setTypeface(tf);
	}
}