package com.dobe.zer0.coolweather.util;

import android.text.TextUtils;

import com.dobe.zer0.coolweather.db.CoolWeatherDB;
import com.dobe.zer0.coolweather.entity.City;
import com.dobe.zer0.coolweather.entity.County;
import com.dobe.zer0.coolweather.entity.Province;

/**
 * Created by dobezer0 on 2017/4/11.
 */

public class TransDatasUtil {
    /*
    trans province datas from server to db
    01|Beijing,02|Shanghai
     */
    public static boolean transProvinceResponse(CoolWeatherDB coolWeatherDB, String response){
        if(!TextUtils.isEmpty(response)){
            String[] provinces = response.split(",");

            if((provinces != null) && (provinces.length > 0)){
                for (String province:provinces) {
                    String[] provincePros = province.split("\\|");

                    Province provinceEntity = new Province();

                    provinceEntity.setProvinceCode(provincePros[0]);
                    provinceEntity.setProvinceName(provincePros[1]);

                    //make server data in db
                    coolWeatherDB.saveProvince(provinceEntity);
                }

                return true;
            }
        }

        return false;
    }

    /*
    trans city datas from server to db
     */
    public static boolean transCityResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId){
        if(!TextUtils.isEmpty(response)){
            String[] cities = response.split(",");

            if((cities != null) && (cities.length > 0)){
                for (String city:cities) {
                    String[] cityPros = city.split("\\|");

                    City cityEntity = new City();

                    cityEntity.setCityCode(cityPros[0]);
                    cityEntity.setCityName(cityPros[1]);
                    cityEntity.setProvinceId(provinceId);

                    //make server data in db
                    coolWeatherDB.saveCity(cityEntity);
                }

                return true;
            }
        }

        return false;
    }

    /*
    trans county datas from server to db
     */
    public static boolean transCountyResponse(CoolWeatherDB coolWeatherDB, String response, int cityId){
        if(!TextUtils.isEmpty(response)){
            String[] counties = response.split(",");

            if((counties != null) && (counties.length > 0)){
                for (String county:counties) {
                    String[] countyPros = county.split("\\|");

                    County countyEntity = new County();

                    countyEntity.setCountyCode(countyPros[0]);
                    countyEntity.setCountyName(countyPros[1]);
                    countyEntity.setCityId(cityId);

                    //make server data in db
                    coolWeatherDB.saveCounty(countyEntity);
                }

                return true;
            }
        }

        return false;
    }
}
