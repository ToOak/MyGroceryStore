package com.example.xushuailong.mygrocerystore.scan.scan1;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public class WeakHandler extends Handler {
	WeakReference<Activity> activityReference;

	public WeakHandler(Activity activity) {
		activityReference = new WeakReference<Activity>(activity);
	}

	@Override
	public void handleMessage(Message msg) {
		final Activity activity = activityReference.get();
	}
}
