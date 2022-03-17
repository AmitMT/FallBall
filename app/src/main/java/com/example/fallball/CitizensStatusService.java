package com.example.fallball;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public class CitizensStatusService extends Service {

	int maxTime = 10;
	int currentTime = maxTime;
	NotificationCompat.Builder statusBarNotification;
	Thread statusBarUpdateThread;
	boolean threadActive = true;

	boolean isPlayed = true;

	@Override
	public void onCreate() {
		super.onCreate();

		createNotificationChannel();

		statusBarNotification = new NotificationCompat.Builder(this, "KEK_GAMER_TIME")
			.setContentTitle("KEK ProGamer")
			.setSmallIcon(R.drawable.heart)
			.setContentText("CitizensStatus")
			.setPriority(NotificationCompat.PRIORITY_DEFAULT)
			.setOnlyAlertOnce(true)
			.setColor(ContextCompat.getColor(this, R.color.purple_200))
			.setProgress(maxTime, currentTime, false);
		int notificationId = 1;

		statusBarUpdateThread = new Thread() {

			@Override
			public void run() {
				for (; threadActive; currentTime = (isPlayed) ? currentTime - 1 : currentTime + 1) {
					if (currentTime >= maxTime) {
						currentTime = maxTime;
						statusBarNotification.setContentText("Energy Full");
						statusBarNotification.setProgress(0, 0, false);

						startForeground(2, statusBarNotification.build());
						if (!isPlayed && currentTime >= maxTime) {
							NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
							notificationManager.cancel(notificationId);
							notificationManager.notify(2, statusBarNotification.build());
							stopStatusBarNotificationUpdateThread();
							stopSelf();
						}
					} else if (currentTime <= 0) {
						currentTime = 0;
						statusBarNotification.setContentText("Energy Depleted");
						GameThread.gameDone = true;
						currentTime = maxTime;
						statusBarNotification.setProgress(0, 0, false);

						NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
						notificationManager.cancel(notificationId);
						notificationManager.notify(2, statusBarNotification.build());
						stopStatusBarNotificationUpdateThread();
						stopSelf();
					} else {
						statusBarNotification.setContentText("Energy status");
						statusBarNotification.setProgress(maxTime, currentTime, false);
						startForeground(notificationId, statusBarNotification.build());
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		startStatusBarNotificationUpdateThread();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		isPlayed = intent.getBooleanExtra("isPlayed", false);

		return START_REDELIVER_INTENT;
	}

	void startStatusBarNotificationUpdateThread() {
		threadActive = true;
		statusBarUpdateThread.start();
	}

	void stopStatusBarNotificationUpdateThread() {
		threadActive = false;
	}

	void createNotificationChannel() {
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			CharSequence name = "nu";
			String description = "ten li 100 gever";
			int importance = NotificationManager.IMPORTANCE_DEFAULT;
			NotificationChannel channel =
				new NotificationChannel("KEK_GAMER_TIME", name, importance);
			channel.setDescription(description);
			// Register the channel with the system; you can't change the importance
			// or other notification behaviors after this
			NotificationManager notificationManager =
				getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
