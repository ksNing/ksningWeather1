package com.example.ks.ksningweather.util;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.ks.ksningweather.Bean.City;
import com.example.ks.ksningweather.MyActivity;
import com.example.ks.ksningweather.R;
import com.example.ks.ksningweather.app.MyApplication;

import java.util.List;

public class Locate extends Activity {
    private LocationClient mLocationClient;
    private MyLocationListener myLocationListener;
    Button locateBtn;
    private String cityCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locate);
        locateBtn=(Button)findViewById(R.id.cityLocation);
        mLocationClient = new LocationClient(this);
        myLocationListener = new MyLocationListener(locateBtn);
        mLocationClient.registerLocationListener(myLocationListener);
        initLocation();
        mLocationClient.start();

        final Intent intent=new Intent(this, MyActivity.class);
        locateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication myApplication=(MyApplication)getApplication();
                List<City> mCityList = myApplication.getCityList();
                for(City city:mCityList){
                   String locationCityName=locateBtn.getText().toString();
                   if(city.getCity().equals(locationCityName)){
                       cityCode=city.getNumber();
                   }
                }
                SharedPreferences sharedPreferences=getSharedPreferences("cityCodePreferenced",Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("cityCode",cityCode);
                editor.commit();
                intent.putExtra("cityCode",cityCode);
                startActivity(intent);

            }
        });
    }
    void initLocation()
    {
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
    }

}
 class MyLocationListener implements BDLocationListener {
    Button locBtn;
    MyLocationListener(Button locBtn){
        this.locBtn=locBtn;
    }
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        String cityName = bdLocation.getCity();
        String recity=cityName.replace("市","");
        locBtn.setText(recity);



    }
}


