package com.example.ks.ksningweather.app;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.example.ks.ksningweather.Bean.City;
import com.example.ks.ksningweather.db.CityDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MyApplication extends Application {
    private static final String TAG = "MyAPP";

    private static MyApplication mApplication;
    private CityDB mCityDB;
    private List<City> mCityList;
    private List<String> listName;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MyApplication->Oncreate");

        mApplication = this;
        mCityDB = openCityDB();
        initCityList();
    }

    private void initCityList() {
        mCityList=new ArrayList<City>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareCityList();
            }
        }).start();
    }
    @TargetApi(Build.VERSION_CODES.N)
    private boolean prepareCityList(){
        mCityList=mCityDB.getAllCity();
        listName = new ArrayList<String>();

        int i=0;
        for(City city:mCityList) {
            i++;
            String cityName = city.getCity();
            String cityCode = city.getNumber();
            Log.d(TAG, cityCode + ":" + cityName);


            listName.add(cityName);
        }
        listName.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String s1=o1.toLowerCase();
                String s2=o2.toLowerCase();
                return s1.compareTo(s2);//从a到z排序
            }
        });


        Log.d(TAG,"i="+i);
        return true;
    }
    public List<City> getCityList(){
        return mCityList;
    }

    public static MyApplication getInstance() {
        return mApplication;
    }
//创建打开数据库的方法
    private CityDB openCityDB() {
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + getPackageName()
                + File.separator + "database1"
                + File.separator
                + CityDB.CITY_DB_NAME;
        File db = new File(path);
        Log.d(TAG, path);
        if (!db.exists()) {

            String pathfolder = "/data"
                    + Environment.getDataDirectory().getAbsolutePath()
                    + File.separator + getPackageName()
                    + File.separator + "database1"
                    + File.separator;
            File dirFirstFolder = new File(pathfolder);
            if (!dirFirstFolder.exists()) {
                dirFirstFolder.mkdirs();
                Log.i("MyApp", "mkdirs");
            }
            Log.i("MyApp", "db is not exists");
            try {
                InputStream is = getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this, path);
    }
}