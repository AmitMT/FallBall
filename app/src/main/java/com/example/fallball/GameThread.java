package com.example.fallball;

import android.content.Context;
import android.os.Handler;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class GameThread extends Thread {

	Context context;
	Handler handler;
	SmileyRow smileyRow;
	ArrayList<SmileyRow> smileyRows = new ArrayList<>();
	Boolean createMoreRows = true;
	RelativeLayout relativeLayout;
	boolean running = false;

	public GameThread(Context context) {
		this.context = context;

		handler = new Handler();
	}

	@Override
	public void run() {
		running = true;
		while (true) {
			if (running) {
				handler.post(() -> {
					smileyRow = new SmileyRow(context);
					smileyRows.add(smileyRow);
					relativeLayout.addView(smileyRow);
					smileyRow.move();
				});
			}

			try {
				sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setRelativeLayout(RelativeLayout relativeLayout) {
		this.relativeLayout = relativeLayout;
	}

	public void pauseGame() {
		for (SmileyRow smileyRow : smileyRows) {
			smileyRow.pauseAnimation();
		}
		running = false;
	}

	public void resumeGame() {
		for (SmileyRow smileyRow : smileyRows) {
			smileyRow.resumeAnimation();
		}
		running = true;
	}
}
