package com.dobe.zer0.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dobe.zer0.coolweather.entity.City;
import com.dobe.zer0.coolweather.entity.County;
import com.dobe.zer0.coolweather.entity.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dobezer0 on 2017/4/10.
 */

public class CoolWeatherDB {
    /*
    db name
     */
    private static final String DB_NAME = "cool_weather";

    /*
    db version
     */
    private static final int DB_VERSION = 1;

    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;

    /*
    init dbOpenHelper
     */
    private CoolWeatherDB(Context context) {
        CoolWeatherOpenHelper coolWeatherOpenHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, DB_VERSION);

        db = coolWeatherOpenHelper.getWritableDatabase();
    }

    /*
    get CoolWeatherDB
     */
    public synchronized static CoolWeatherDB getInstance(Context context) {
        if (coolWeatherDB == null) {
            coolWeatherDB = new CoolWeatherDB(context);
        }

        return coolWeatherDB;
    }

    /*
    save Province data in db
     */
    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues contentValues = new ContentValues();

            contentValues.put("province_name", province.getProvinceName());
            contentValues.put("province_code", province.getProvinceCode());

            db.insert("Province", null, contentValues);
        }
    }

    /*
    get Province datas
     */
    public List<Province> loadProvinces() {
        List<Province> provinces = new ArrayList<Province>();

        Cursor cursor = db.query("Province", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();

                province.setProvinceId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));

                provinces.add(province);
            } while (cursor.moveToNext());
        }

        return provinces;
    }

    /*
    save City data in db
     */
    public void saveCity(City city) {
        if (city != null) {
            ContentValues contentValues = new ContentValues();

            contentValues.put("city_name", city.getCityName());
            contentValues.put("city_code", city.getCityCode());
            contentValues.put("province_id", city.getProvinceId());

            db.insert("City", null, contentValues);
        }
    }

    /*
    get City datas in one Province
     */
    public List<City> loadCities(int provinceId) {
        List<City> cities = new ArrayList<City>();

        Cursor cursor = db.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                City city = new City();

                city.setCityId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));

                cities.add(city);
            } while (cursor.moveToNext());
        }

        return cities;
    }

    /*
    save County data in db
     */
    public void saveCounty(County county) {
        if (county != null) {
            ContentValues contentValues = new ContentValues();

            contentValues.put("county_name", county.getCountyName());
            contentValues.put("county_code", county.getCountyCode());
            contentValues.put("city_id", county.getCityId());

            db.insert("County", null, contentValues);
        }
    }

    /*
    get County datas in one City
     */
    public List<County> loadCounties(int cityId) {
        List<County> counties = new ArrayList<County>();

        Cursor cursor = db.query("County", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                County county = new County();

                county.setCountyId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));

                counties.add(county);
            } while (cursor.moveToNext());
        }

        return counties;
    }
}
