package com.example.fallball;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

public class SmileyRow extends RelativeLayout {

	Smiley[] smileyArray = new Smiley[8];
	int smileysNumber;
	ValueAnimator animator;

	public SmileyRow(Context context) {
		super(context);

		setLayoutParams(
			new LinearLayout.LayoutParams(
				(int) Utils.dpToPixels(context, 40 * smileyArray.length),
				(int) Utils.dpToPixels(context, 40)
			)
		);

		for (int i = 0; i < smileyArray.length; i++) {
			smileyArray[i] = createSmileyImage(i, i * Utils.dpToPixels(context, 40));
			addView(smileyArray[i]);
			smileysNumber++;
		}
		move();
	}

	public SmileyRow(Context context, @Nullable AttributeSet attrs) {
		this(context);
	}

	Smiley createSmileyImage(int index, float xLocationOnScreen) {
		Smiley smiley = new Smiley(getContext(), Smiley.Face.REGULAR, this, index);
		smiley.setX(xLocationOnScreen);
		return smiley;
	}

	public void removeSmiley(int indexInRow) {
		new CountDownTimer(300, 16) {
			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				removeView(smileyArray[indexInRow]);
				smileyArray[indexInRow] = null;
				smileysNumber--;
			}
		}.start();
	}

	public void move() {
		float[] displacement = { -Utils.dpToPixels(getContext(), 40), Utils.getScreenHeight() - Utils.dpToPixels(getContext(), 180) };
		animator = ValueAnimator
			.ofFloat(displacement[0], displacement[1])
			.setDuration((long) ((Utils.pixelsToDp(getContext(), displacement[1] - displacement[0]) / 52) * 1000));
		animator.setInterpolator(new LinearInterpolator());
		animator.addUpdateListener(animation -> setY((Float) animation.getAnimatedValue()));
		animator.start();
		setVisibility(VISIBLE);
	}

	public SmileyRow getSmileyRow() {
		return this;
	}

	public boolean checkSmileyNumber(int authorizedNumber) {
		if (smileysNumber == 0)
			return true;

		boolean isEqual = smileysNumber == authorizedNumber;
		if (isEqual) {
			for (Smiley smiley : smileyArray)
				if (smiley != null) {
					smiley.setClickable(false);
					smiley.changeToHappy();
				}
		} else {
			for (Smiley smiley : smileyArray)
				if (smiley != null) {
					smiley.setClickable(false);
					smiley.changeToSad();
				}
		}
		new CountDownTimer(300, 16) {
			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				((ViewGroup) getParent()).removeView(getSmileyRow());
			}
		}.start();

		return isEqual;
	}

	public int getSmileysNumber() {
		return smileysNumber;
	}

	public void pauseAnimation() {
		animator.pause();
	}

	public void resumeAnimation() {
		animator.resume();
	}
}
