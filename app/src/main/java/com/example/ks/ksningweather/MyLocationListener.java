package com.example.ks.ksningweather;

import android.util.Log;
import android.widget.Button;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.example.ks.ksningweather.Bean.City;
import com.example.ks.ksningweather.app.MyApplication;

import java.util.List;

public class MyLocationListener implements BDLocationListener {
    private Button button;
    public String recity;
    public String cityCode;




    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        //此处的BDLocation为定位结果的信息类，通过它的各种get方法可获得定位相关的全部结果
        String addr=bdLocation.getAddrStr();
        String country=bdLocation.getCountry();
        String province=bdLocation.getProvince();
        String city=bdLocation.getCity();
        String district=bdLocation.getDistrict();
        String street=bdLocation.getStreet();
        Log.d("ks",city);
        recity=city.replace("市","");


        MyApplication myApplication=MyApplication.getInstance();
        List<City> mCityList=myApplication.getCityList();

        for(City city1:mCityList){
            if(recity.equals(city1.getCity())){
                cityCode=city1.getNumber();
                Log.d("ks1",cityCode);
            }
        }
    }
}
