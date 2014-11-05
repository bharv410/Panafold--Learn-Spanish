package com.panafold.helpers;

import com.panafold.R;
import com.panafold.main.MainActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class AlarmService extends Service {
	   private static final int NOTIFICATION_ID = 1;
	   private NotificationManager notificationManager;
	   private PendingIntent pendingIntent;

	    @Override
	    public IBinder onBind(Intent arg0)
	    {
	        return null;
	    }

	   @SuppressWarnings("static-access")
	   @Override
	   public void onStart(Intent intent, int startId)
	   {
		   System.out.println("onSTART");
		   System.out.println("onSTART");
	       super.onStart(intent, startId);
	       getSharedPreferences("PrefsFile", 0).edit().putInt("showword", (getSharedPreferences("PrefsFile", 0).getInt("showword", 7)+1)).commit();
	       Context context = this.getApplicationContext();
	       notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
	       Intent mIntent = new Intent(this, MainActivity.class);
		pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setContentTitle("You've got a new word!");
		builder.setContentText("Tanuki");
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentIntent(pendingIntent);

		notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(NOTIFICATION_ID, builder.build());
	    }
	}