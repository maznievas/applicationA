package com.example.andrey.applicationa.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Receiver extends BroadcastReceiver {

    final String TAG = "mLog";

    @Override
    public void onReceive(Context ctx, Intent intent) {
        Log.d(TAG, "onReceive");
        Log.d(TAG, "action = " + intent.getAction());
        Log.d(TAG, "extra = " + intent.getStringExtra("extra"));
    }
}
