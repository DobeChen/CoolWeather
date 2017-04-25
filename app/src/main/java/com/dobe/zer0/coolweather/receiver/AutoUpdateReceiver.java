package com.dobe.zer0.coolweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dobe.zer0.coolweather.service.AutoUpdateService;

public class AutoUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //start AutoUpdateService
        Intent serviceIntent = new Intent(context, AutoUpdateService.class);

        context.startService(serviceIntent);
    }
}
