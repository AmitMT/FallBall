package com.example.fallball;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class SmileyRow extends LinearLayout {

	Smiley[] smileyArray = new Smiley[8];
	int smileysNumber;
	ValueAnimator animator;

	public SmileyRow(Context context) {
		super(context);

		setVisibility(GONE);
		setLayoutParams(
			new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT
			)
		);

		for (int i = 0; i < smileyArray.length; i++) {
			smileyArray[i] = new Smiley(context, Smiley.Face.REGULAR, this, i);
			addView(smileyArray[i]);
		}
		move();
	}

	public SmileyRow(Context context, @Nullable AttributeSet attrs) {
		this(context);
	}

	Smiley createSmileyImage(int index, float xLocationOnScreen) {
		return new Smiley(getContext(), Smiley.Face.HAPPY, this, index);
	}

	public void removeSmiley(int indexInRow) {
		new CountDownTimer(300, 16) {
			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {
				smileyArray[indexInRow].setType(Smiley.Face.EMPTY);
			}
		}.start();
	}

	public void move() {
		animator = ValueAnimator
			.ofFloat(0, Utils.getScreenHeight() - Utils.convertDpToPixel(getContext(), 120))
			.setDuration(5000);
		animator.setInterpolator(new LinearInterpolator());
		animator.addUpdateListener(animation -> setY((Float) animation.getAnimatedValue()));
		animator.start();
		setVisibility(VISIBLE);
	}

	public void pauseAnimation() {
		animator.pause();
	}

	public void resumeAnimation() {
		animator.resume();
	}
}
