package com.example.fallball;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fallball.ui.TextDialog;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

	GameThread gameThread;

	boolean paused = false;

	Sensor sensor;
	SensorManager sensorManager;
	float light;

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

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
	}

	public GameThread getGameThread() {
		return gameThread;
	}

	@Override
	public void onPause() {
		super.onPause();

		paused = true;

		if (sensor != null) sensorManager.unregisterListener(this);

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

		if (sensor != null)
			sensorManager.registerListener(this, sensor, Sensor.TYPE_LIGHT);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		Sensor sensor = event.sensor;
		if (sensor.getType() == Sensor.TYPE_LIGHT) {
			light = event.values[0];
			if (light > 50)
				findViewById(R.id.main_layout).setBackgroundResource(R.drawable.light_background);
			else findViewById(R.id.main_layout).setBackgroundResource(R.drawable.dark_background);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
}