package com.example.fallball;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fallball.ui.TextDialog;

public class MainActivity extends AppCompatActivity {

	GameThread gameThread;

	boolean paused = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
			WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN
		);

		setContentView(R.layout.activity_main);

		gameThread = new GameThread(this);
		gameThread.start();
	}

	public GameThread getGameThread() {
		return gameThread;
	}

	@Override
	public void onPause() {
		super.onPause();

		paused = true;

		gameThread.pauseGame();
	}

	@Override
	public void onResume() {
		super.onResume();

		if (paused) {
			TextDialog.Options textDialogOptions = new TextDialog.Options()
				.setTitle("Welcome back!")
				.setText("Resume game?")
				.setButtonText("OK!");
			TextDialog textDialog = new TextDialog(this, textDialogOptions);
			textDialog.show();
			textDialog.setOnCancelListener((dialogInterface) -> gameThread.resumeGame());
		}
	}
}