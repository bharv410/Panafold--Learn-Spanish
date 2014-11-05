package com.panafold.helpers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReciever extends BroadcastReceiver {

	NotificationManager notificationManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service1 = new Intent(context, AlarmService.class);
	        context.startService(service1);
	        System.out.println("onRECIEVE");
	        System.out.println("onRECIEVE");
	}
}
