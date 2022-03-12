package com.example.fallball.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.example.fallball.R;
import com.example.fallball.Utils;

public class Button extends androidx.appcompat.widget.AppCompatTextView {

	public Button(Context context) {
		super(context, null);
	}

	public Button(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);

		int[] changedAttributes = new int[] {
			android.R.attr.padding,
			android.R.attr.paddingLeft,
			android.R.attr.paddingTop,
			android.R.attr.paddingRight,
			android.R.attr.paddingBottom
		};

		TypedArray arr = context.obtainStyledAttributes(attributeSet, changedAttributes);

		int defaultPadding = arr.hasValue(0) ?
			arr.getDimensionPixelOffset(0, -1) : (int) Utils.convertDpToPixel(context, 15);
		int[] padding = {
			arr.hasValue(1) ?
				arr.getDimensionPixelOffset(1, -1) : defaultPadding,
			arr.hasValue(2) ?
				arr.getDimensionPixelOffset(2, -1) : defaultPadding,
			arr.hasValue(3) ?
				arr.getDimensionPixelOffset(3, -1) : defaultPadding,
			arr.hasValue(4) ?
				arr.getDimensionPixelOffset(4, -1) : defaultPadding,
		};

		arr.recycle();

		setBackground(ContextCompat.getDrawable(context, R.drawable.button_background));
		setAllCaps(false);
		setTextColor(ContextCompat.getColor(context, R.color.title));
		setPadding(
			padding[0], padding[1], padding[2], padding[3]
		);
	}
}
