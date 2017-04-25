package com.dobe.zer0.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.dobe.zer0.coolweather.receiver.AutoUpdateReceiver;
import com.dobe.zer0.coolweather.util.HttpCallbackListener;
import com.dobe.zer0.coolweather.util.HttpUtil;
import com.dobe.zer0.coolweather.util.TransDatasUtil;

public class AutoUpdateService extends Service {
    private static final String WEATHER_SERVER_ADDRESS = "http://www.weather.com.cn/data/cityinfo/";
    private static final String WEATHER_STUFFIX_STR = ".html";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
            }
        }).start();

        //create a time task
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int aHour = 8 * 60 * 60 * 1000;
//        int aHour = 5 * 1000;
        long tiggerTime = SystemClock.elapsedRealtime() + aHour;
        Intent broadcastIntent = new Intent(this, AutoUpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, tiggerTime, pendingIntent);

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * update weather info
     */
    private void updateWeather(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String weatherCode = preferences.getString("weather_code", "");
        String address = WEATHER_SERVER_ADDRESS + weatherCode + WEATHER_STUFFIX_STR;

        HttpUtil.sendRequest(address, "GET", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                TransDatasUtil.transWeatherResponse(AutoUpdateService.this, response);
            }

            @Override
            public void onExcepton(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
