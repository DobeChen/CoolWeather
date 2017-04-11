package com.dobe.zer0.coolweather.util;

/**
 * Created by dobezer0 on 2017/4/11.
 */

public interface HttpCallbackListener {
    void onFinish(String response);

    void onExcepton(Exception e);
}
