package com.dobe.zer0.coolweather.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.UiThread;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dobe.zer0.coolweather.R;
import com.dobe.zer0.coolweather.util.HttpCallbackListener;
import com.dobe.zer0.coolweather.util.HttpUtil;
import com.dobe.zer0.coolweather.util.TransDatasUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class WeatherLayoutActivity extends BaseActivity {
    private static final String COUNTY_SERVER_ADDRESS = "http://www.weather.com.cn/data/list3/city";
    private static final String COUNTY_STUFFIX_STR = ".xml";

    private static final String WEATHER_SERVER_ADDRESS = "http://www.weather.com.cn/data/cityinfo/";
    private static final String WEATHER_STUFFIX_STR = ".html";

    @BindView(R.id.weather_info_layout)
    LinearLayout weatherInfoLayout;

    @BindViews({R.id.city_name, R.id.publish_text, R.id.current_date, R.id.weather_info, R.id.min_temp, R.id.max_temp})
    List<TextView> textViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);

        //init Layout and TextViews
        ButterKnife.bind(this);

        //get countyCode from chooseAreaActivity
        String countyCode = getIntent().getStringExtra("conunty_code");

        if (!TextUtils.isEmpty(countyCode)) {
            //get weather info from server by countyCode
            textViews.get(1).setText("Loading...");
            textViews.get(0).setVisibility(View.INVISIBLE);
            weatherInfoLayout.setVisibility(View.INVISIBLE);

            getWeatherInfoCode(countyCode);
        } else {
            //show info from SharedPreferences
            showWeatherInfo();
        }
    }

    /**
     * get weather code from server
     */
    private void getWeatherInfoCode(String countyCode) {
        String address = COUNTY_SERVER_ADDRESS + countyCode + COUNTY_STUFFIX_STR;

        getInfoFromServer(address, "CountyCode");
    }

    /**
     * get weather info from server
     */
    private void getWeatherInfo(String weatherCode) {
        String address = WEATHER_SERVER_ADDRESS + weatherCode + WEATHER_STUFFIX_STR;

        getInfoFromServer(address, "WeatherCode");
    }

    /**
     * get weatherCode or weatherInfo from server
     * and save in SharedPreferences
     */
    private void getInfoFromServer(String address, final String infoType) {
        HttpUtil.sendRequest(address, "GET", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if (!TextUtils.isEmpty(response)) {
                    if ("CountyCode".equals(infoType)) {
                        //get weatherCode from server info
                        String[] infoArray = response.split("\\|");

                        if((infoArray != null) && (infoArray.length == 2)){
                            String weatherCode = infoArray[1];

                            getWeatherInfo(weatherCode);
                        }
                    } else if ("WeatherCode".equals(infoType)) {
                        //save weather info in SharedPreferences and show weather info
                        TransDatasUtil.transWeatherResponse(WeatherLayoutActivity.this, response);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showWeatherInfo();
                            }
                        });
                    }
                }
            }

            @Override
            public void onExcepton(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViews.get(1).setText("Load Filed.");
                    }
                });
            }
        });
    }

    /**
     * show weather infos from SharedPreferences
     */
    private void showWeatherInfo() {
        //city_name, publish_text, current_date, weather_info, min_temp, max_temp
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        textViews.get(0).setText(preferences.getString("city_name", ""));
        textViews.get(1).setText(preferences.getString("publish_time", ""));
        textViews.get(2).setText("Today " + preferences.getString("current_time", "") + " published");
        textViews.get(3).setText(preferences.getString("weather_desc", ""));
        textViews.get(4).setText(preferences.getString("min_temp", ""));
        textViews.get(5).setText(preferences.getString("max_temp", ""));

        weatherInfoLayout.setVisibility(View.VISIBLE);
        textViews.get(0).setVisibility(View.VISIBLE);
    }
}
