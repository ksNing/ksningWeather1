package com.example.ks.ksningweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ks.ksningweather.Bean.City;
import com.example.ks.ksningweather.Bean.TodayWeather;
import com.example.ks.ksningweather.util.NetUtil;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MyActivity extends Activity implements View.OnClickListener {

    public static final int UPDATE_TODAY_WEATHER=1;

    private ImageView mUpdateBtn;

    private ImageView mCitySelect;
    private String updateCityCode;
    private ProgressBar progressBar;



    private TextView cityTv,timeTv,humidityTv,weekTv,pmDataTv,pmQualityTv,
    temperatureTv,climateTv,windTv,city_name_Tv,temperatureRangeTv;
    private ImageView weatherImg, pmImg;

    private Handler mHandler=new Handler(){
        public void handleMessage(android.os.Message msg){
            switch(msg.what){
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);
                    break;
                default:
                    break;
            }
        }


    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ksninglayout);

        //为更新按钮添加单击事件
        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);
        progressBar=(ProgressBar)findViewById(R.id.title_update_progress);


        //为选择城市ImageView添加单击事件
        mCitySelect = (ImageView) findViewById(R.id.title_city_manager);
        mCitySelect.setOnClickListener(this);


        //调用检测网络连接状态方法
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("ksningWeather", "网络ok");
           // Toast.makeText(MyActivity.this, "网络ok", Toast.LENGTH_LONG).show();
        } else {
            Log.d("ksningWeather", "网络失败");
            Toast.makeText(MyActivity.this, "网络失败", Toast.LENGTH_SHORT).show();
        }

       initView();






    }







    @Override
    public void onClick(View view) {

        //单击事件如果是切换城市事件发生
        if(view.getId() == R.id.title_city_manager){
            Intent i=new Intent(this,SelectCity.class);
            startActivityForResult(i,1);
        }


        //单击事件如果是刷新天气应用发生
        if (view.getId() == R.id.title_update_btn) {

            //通过SharedPreferences读取城市id，如果没有定义则缺省为101010100（北京城市)
            SharedPreferences sharedPreferences = getSharedPreferences("cityCodePreferenced", Activity.MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("cityCode","");
            if(!cityCode.equals("")){
                Log.d("ksning",cityCode);
                queryWeatherCode(cityCode);
            }else{
                queryWeatherCode("101010100");
            }

            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("ksningWeather", "网络ok");
                queryWeatherCode(cityCode);
            } else {
                Log.d("ksningWeather", "网络失败");
                Toast.makeText(MyActivity.this, "网络失败", Toast.LENGTH_SHORT).show();

            }
            mUpdateBtn.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);


        }

    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        //updateCityCode=getIntent().getStringExtra("cityCode");
        if(requestCode==1 && resultCode==RESULT_OK){
            String newCityCode=data.getStringExtra("cityCode");
            Log.d("ks1:",newCityCode);
            Log.d("ksningWeather","选择的城市代码为:"+newCityCode);

            if(NetUtil.getNetworkState(this)!=NetUtil.NETWORN_NONE){
                Log.d("ksningweather","网络ok");
                queryWeatherCode(newCityCode);
            }else{
                Log.d("ksningweather","网络失败");
                Toast.makeText(MyActivity.this, "网络失败", Toast.LENGTH_SHORT).show();

            }
        }
    }


    /**
     * 使用**获取网络数据
     */

    private void queryWeatherCode(String cityCode){
        final String address="http://wthrcdn.etouch.cn/WeatherApi?citykey="+cityCode;
        Log.d("ksningWeather",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con=null;
                TodayWeather todayWeather = null;
                try{
                    URL url=new URL(address);
                    con=(HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in=con.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String str;
                    while((str=reader.readLine())!=null){
                        response.append(str);
                        Log.d("ksningWeather1",str);
                    }
                    String responseStr=response.toString();
                    Log.d("ksningWeather",responseStr);

                    todayWeather=parseXML(responseStr);
                    if(todayWeather!=null){
                        Log.d("ksningWeather",todayWeather.toString());
                        Message msg=new Message();
                        msg.what=UPDATE_TODAY_WEATHER;
                        msg.obj=todayWeather;
                        mHandler.sendMessage(msg);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(con!=null){
                        con.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 编写解析函数，解析出城市名称已经更新时间信息
     * @param xmldata
     */

    private TodayWeather parseXML(String xmldata){
        TodayWeather todayWeather=null;
        int fengxiangCount=0;
        int fengliCount=0;
        int dateCount=0;
        int highCount=0;
        int lowCount=0;
        int typeCount=0;
        try{
            XmlPullParserFactory fac=XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser=fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType=xmlPullParser.getEventType();
            Log.d("ksningWeather","parseXML");
            while(eventType!=XmlPullParser.END_DOCUMENT){
                switch(eventType){
                    //判断当前事件为开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    //判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if(xmlPullParser.getName().equals("resp")){
                            todayWeather=new TodayWeather();

                        } if(todayWeather!=null) {
                        if (xmlPullParser.getName().equals("city")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setCity(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("updatetime")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setUpdatetime(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("shidu")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setShidu(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("wendu")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setWendu(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("pm25")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setPm25(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("quality")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setQuality(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                            eventType = xmlPullParser.next();
                            todayWeather.setFengxiang(xmlPullParser.getText());
                            fengxiangCount++;
                        } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                            eventType = xmlPullParser.next();
                            todayWeather.setFengli(xmlPullParser.getText());
                            fengliCount++;
                        } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                            eventType = xmlPullParser.next();
                            todayWeather.setDate(xmlPullParser.getText());
                            dateCount++;
                        } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                            eventType = xmlPullParser.next();
                            todayWeather.setHigh(xmlPullParser.getText());
                            highCount++;
                        } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                            eventType = xmlPullParser.next();
                            todayWeather.setLow(xmlPullParser.getText());
                            lowCount++;
                        } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                            eventType = xmlPullParser.next();
                            todayWeather.setType(xmlPullParser.getText());
                            typeCount++;
                        }
                    }
                        break;
                        //判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
                //进入下一个元素并触发相应事件
                eventType=xmlPullParser.next();
            }
        }catch(XmlPullParserException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return todayWeather;
    }

    /**
     * 初始化控件内容
     */
    void initView(){
        city_name_Tv=(TextView)findViewById(R.id.title_city_name);
        cityTv=(TextView)findViewById(R.id.city);
        timeTv=(TextView)findViewById(R.id.time);
        humidityTv=(TextView)findViewById(R.id.humidity);
        weekTv=(TextView)findViewById(R.id.week_today);
        pmDataTv=(TextView)findViewById(R.id.pm_data);
        pmQualityTv=(TextView)findViewById(R.id.pm2_5_quality);
        pmImg=(ImageView)findViewById(R.id.pm2_5img);
        temperatureTv=(TextView)findViewById(R.id.temperature);
        climateTv=(TextView)findViewById(R.id.climate);
        windTv=(TextView)findViewById(R.id.wind);
        weatherImg=(ImageView)findViewById(R.id.weather_img);
        temperatureRangeTv=(TextView)findViewById(R.id.temperatureRange);

        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        weekTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
        temperatureRangeTv.setText("N/A");

    }

    /**
     *
     * @param todayWeather
     */
    void updateTodayWeather(TodayWeather todayWeather){


        if(mUpdateBtn.getVisibility()==View.GONE&&progressBar.getVisibility()==View.VISIBLE){
            progressBar.setVisibility(View.GONE);
            mUpdateBtn.setVisibility(View.VISIBLE);
        }

        city_name_Tv.setText(todayWeather.getCity()+"天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime()+"发布");
        humidityTv.setText("湿度:"+todayWeather.getShidu());
        weekTv.setText(todayWeather.getDate());
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());
        temperatureTv.setText("温度:"+todayWeather.getWendu());
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力:"+todayWeather.getFengli());
        temperatureRangeTv.setText(todayWeather.getLow()+"~"+todayWeather.getHigh());
        Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();





    }

}
