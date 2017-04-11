package com.dobe.zer0.coolweather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by dobezer0 on 2017/4/11.
 */

public class HttpUtil {

    public static void sendRequest(final String address, final String sendMethod, final HttpCallbackListener callbackListener) {

        //cost time operation put in thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;

                try {
                    URL url = new URL(address);

                    urlConnection = (HttpURLConnection) url.openConnection();

                    //set connection pros
                    urlConnection.setRequestMethod(sendMethod);
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);

                    if(urlConnection.getResponseCode() == 200){
                        InputStream responseStream = urlConnection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));

                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null){
                            response.append(line);
                        }

                        //trans response
                        if (callbackListener != null){
                            callbackListener.onFinish(response.toString());
                        }
                    }else{
                        throw new UnknownHostException(address);
                    }
                } catch (Exception e) {
                    if(callbackListener != null){
                        callbackListener.onExcepton(e);
                    }

                    e.printStackTrace();
                } finally {
                    if (urlConnection != null){
                        urlConnection.disconnect();
                    }
                }
            }
        }).start();
    }
}
