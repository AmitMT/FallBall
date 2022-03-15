package com.example.fallball;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class Smiley extends androidx.appcompat.widget.AppCompatImageView {

	Face type;
	SmileyRow smileyRow;
	int indexInRow;

	public Smiley(@NonNull Context context, Face type, SmileyRow smileyRow, int indexInRow) {
		super(context);

		setType(type);
		this.smileyRow = smileyRow;
		this.indexInRow = indexInRow;

		setLayoutParams(
			new ViewGroup.LayoutParams(
				(int) Utils.dpToPixels(context, 40),
				(int) Utils.dpToPixels(context, 40)
			)
		);

		setOnClickListener(view -> smileyClicked());
	}

	public Smiley(@NonNull Context context, @Nullable AttributeSet attrs) {
		this(context, Face.HAPPY, new SmileyRow(context), 0);
	}

	void smileyClicked() {
		if (type == Face.REGULAR) setType(Face.SHOCK);
		smileyRow.removeSmiley(indexInRow);
	}

	public void setType(Face type) {
		this.type = type;
		int backgroundId = getBackgroundIdByFace(type);
		if (backgroundId != 0)
			setBackground(ContextCompat.getDrawable(getContext(), backgroundId));
		else setBackgroundResource(0);
		setImageDrawable(ContextCompat.getDrawable(getContext(), getDrawableIdByFace(type)));
	}

	public void changeToHappy() {
		setType(Face.HAPPY);
	}

	public void changeToSad() {
		setType(Face.SAD);
	}

	public int getDrawableIdByFace(Face face) {
		switch (face) {
			case REGULAR:
				return R.drawable.regular_face;
			case SAD:
				return R.drawable.sad_face;
			case SHOCK:
				return R.drawable.shock_face;
			case HAPPY:
				return R.drawable.happy_face;
			case LOCK:
				return R.drawable.lock_face;
			case SICK:
				return R.drawable.sick_face;
			case ROW_BOMB:
				return R.drawable.row_bomb_face;
			case PLUS_BOMB:
				return R.drawable.plus_bomb_face;
			case FROZEN:
				return R.drawable.frozen_face;
			case DOWN:
				return R.drawable.down_face;
			case UP:
				return R.drawable.up_face;
			case GUARD:
				return R.drawable.guard_face;
			case KING:
				return R.drawable.king_face;
			case PRINCESS:
				return R.drawable.princess_face;
			case JOKER:
				return R.drawable.joker_face;
			default:
				return R.drawable.empty;
		}
	}

	public int getBackgroundIdByFace(Face face) {
		switch (face) {
			case SICK:
				return R.drawable.red_face;
			case ROW_BOMB:
			case PLUS_BOMB:
				return R.drawable.black_face;
			case FROZEN:
				return R.drawable.blue_face;
			case EMPTY:
				return 0;
			default:
				return R.drawable.yellow_face;
		}
	}

	public enum Face {
		REGULAR,
		SAD,
		SHOCK,
		HAPPY,
		LOCK,
		SICK,
		ROW_BOMB,
		PLUS_BOMB,
		FROZEN,
		DOWN,
		UP,
		EMPTY,
		GUARD,
		KING,
		PRINCESS,
		JOKER,
	}
}
