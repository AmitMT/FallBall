package com.example.fallball;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fallball.ui.TextDialog;

public class MainActivity extends AppCompatActivity {

	GameThread gameThread = new GameThread(this);

	boolean paused = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gameThread.setRelativeLayout(findViewById(R.id.rowsLayout));
		gameThread.start();
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

	public void onClick(View view) {
	}
}