package com.example.fallball;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fallball.ui.TextDialog;

import java.util.ArrayList;
import java.util.Random;

public class GameThread extends Thread {

	final int SPAWN_INTERVAL = 2000;
	final int CHECK_INTERVAL = 50;
	Activity activity;
	RelativeLayout relativeLayout;
	RelativeLayout bottomBar;
	TextView authorizedCountView;
	ImageView borderView;
	TextView scoreView;
	ImageView[] hearts;
	Handler handler;
	SmileyRow smileyRow;
	ArrayList<SmileyRow> smileyRows = new ArrayList<>();
	Boolean createMoreRows = false;
	Boolean isChecking = false;
	int timesToChangeNumber = 3;
	int currentAuthorizedNumber;
	int score = 0;
	int lives = 3;

	public GameThread(Activity activity) {
		this.activity = activity;

		relativeLayout = activity.findViewById(R.id.rowsLayout);
		bottomBar = activity.findViewById(R.id.bottomBar);
		authorizedCountView = activity.findViewById(R.id.authorizedSmileys);
		borderView = activity.findViewById(R.id.border);
		scoreView = activity.findViewById(R.id.points);
		hearts = new ImageView[] {
			activity.findViewById(R.id.heart1),
			activity.findViewById(R.id.heart2),
			activity.findViewById(R.id.heart3)
		};

		handler = new Handler();

		currentAuthorizedNumber = getNewRandomNumber();
		authorizedCountView.setText(String.valueOf(currentAuthorizedNumber));
		bottomBar.setOnClickListener(view -> {
			timesToChangeNumber--;
			if (timesToChangeNumber >= 0) {
				currentAuthorizedNumber = getNewRandomNumber();
				authorizedCountView.setText(String.valueOf(currentAuthorizedNumber));
			}
		});
	}

	@Override
	public void run() {
		startHeartBeat();

		try {
			sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		createMoreRows = true;
		isChecking = true;

		for (int i = SPAWN_INTERVAL / CHECK_INTERVAL; true; i++) {
			if (isChecking) handler.post(this::checkRowPass);

			if (i == SPAWN_INTERVAL / CHECK_INTERVAL && createMoreRows) {
				i = -1;
				handler.post(() -> {
					smileyRow = new SmileyRow(activity);
					smileyRows.add(smileyRow);
					relativeLayout.addView(smileyRow);
					smileyRow.move();
				});
			}

			try {
				sleep(CHECK_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void pauseGame() {
		for (SmileyRow smileyRow : smileyRows)
			smileyRow.pauseAnimation();

		createMoreRows = false;
		isChecking = false;
	}

	public void resumeGame() {
		for (SmileyRow smileyRow : smileyRows)
			smileyRow.resumeAnimation();

		createMoreRows = true;
		isChecking = true;
	}

	void checkRowPass() {
		if (smileyRows.size() > 0 && smileyRows.get(0).getY() >= borderView.getY()) {
			if (!smileyRows.get(0).hasSmileys()) {
				smileyRows.remove(0);
				checkRowPass(); // ריקורסיה 😍
				return;
			}
			timesToChangeNumber = 3;
			if (smileyRows.get(0).checkSmileyNumber(currentAuthorizedNumber)) {
				score += smileyRows.get(0).getSmileysNumber();
				scoreView.setText(String.valueOf(score));
			} else decreaseHealth();
			currentAuthorizedNumber = getNewRandomNumber();
			authorizedCountView.setText(String.valueOf(currentAuthorizedNumber));

			smileyRows.remove(0);
		}
	}

	void decreaseHealth() {
		if (lives > 0) {
			hearts[lives - 1].setImageResource(0);
			startHeartExplosion(hearts[lives - 1]);
			hearts[lives - 1] = null;
			lives--;
		}
		if (lives == 0) {
			pauseGame();

			TextDialog.Options textDialogOptions = new TextDialog.Options()
				.setTitle("You lost!")
				.setText("Start a new game?")
				.setButtonText("LETS DO IT!");
			TextDialog textDialog = new TextDialog(activity, textDialogOptions);
			textDialog.show();
			textDialog.setOnCancelListener((dialogInterface) -> {
				Intent intent = new Intent(activity, MainActivity.class);
				activity.startActivity(intent);
			});
		}
	}

	void startHeartExplosion(ImageView heart) {
		heart.setBackgroundResource(R.drawable.heart_explode_animation);
		AnimationDrawable frameAnimation = (AnimationDrawable) heart.getBackground();
		frameAnimation.start();
	}

	void startHeartBeat() {
		for (ImageView heart : hearts)
			if (heart != null) {
				heart.setBackgroundResource(R.drawable.heart_beat_animation);
				AnimationDrawable frameAnimation = (AnimationDrawable) heart.getBackground();
				frameAnimation.start();
			}
	}

	public int getNewRandomNumber() {
		return new Random().nextInt(8) + 1;
	}
}
