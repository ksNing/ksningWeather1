package com.example.ks.ksningweather.util;

import android.content.Context;
import android.widget.Button;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.ks.ksningweather.MyLocationListener;

public class BDLocationUtils {
    public Context context;
    //LocationClient类是定位SDK的核心类
    public LocationClient mLocationClient=null;
    public BDLocationListener myListener= new MyLocationListener();

    public BDLocationUtils(Context context){
        this.context=context;
    }
    public void doLocation(){
        //声明LocationClient类
        mLocationClient=new LocationClient(context.getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        //初始化定位
        initLocation();
    }
    private void initLocation(){
        LocationClientOption option=new LocationClientOption();
        //默认高精度，低功耗
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setEnableSimulateGps(false);
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(0);
        mLocationClient.setLocOption(option);
    }
}
