package com.example.ks.ksningweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ks.ksningweather.Bean.TodayWeather;
import com.example.ks.ksningweather.adapter.ViewPagerAdapter;

import com.example.ks.ksningweather.util.Locate;
import com.example.ks.ksningweather.util.NetUtil;
import com.example.ks.ksningweather.util.ShareDiaog;
import com.example.ks.ksningweather.util.ShareUtils;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

public class MyActivity extends Activity implements View.OnClickListener,ViewPager.OnPageChangeListener{

    public static final int UPDATE_TODAY_WEATHER=1;
    private static final int DB=2;
    //分享
    ShareDiaog shareDiaog;
    //分享标题
    private String share_title="天气预报系统";
    //分享链接
    private String share_url="http://blog.csdn.net/qq_31390699";
    //分享封面图片
    private String share_img="http://img.zcool.cn/community/0183b855420c990000019ae98b9ce8.jpg@900w_1l_2o_100sh.jpg";
    //分享描述
    private String share_desc="简单的天气预报系统";
    //显示两个页面
    private ViewPagerAdapter vpAdapter;
    private ViewPager vp;
    private List<View> views1;
    //为引导增加小圆点
    private ImageView[] dots1;
    private int[] ids={R.id.not1,R.id.not2};

    private ImageView mUpdateBtn;

    private ImageView mCitySelect;
    private String updateCityCode;
    private ProgressBar progressBar;
    private ImageView mTitleLocation;
    private ImageView share;



/*
    //初始化LocationClient类
    public LocationClient mLocationClient=null;
    private MyLocationListerner myLocationListerner=new MyLocationListerner();
*/


    private TextView cityTv,timeTv,humidityTv,weekTv,pmDataTv,pmQualityTv,
    temperatureTv,climateTv,windTv,city_name_Tv,temperatureRangeTv;
    //绑定未来三天天气的组键
    private TextView week1Tv,week2Tv,week3Tv,temperature1Tv,temperature2Tv,temperature3Tv,
    wind1Tv,wind2Tv,wind3Tv,high1Tv,high2Tv,high3Tv;
    //天气photo
    private ImageView climate1Tv,climate2Tv,climate3Tv,climate4Tv,climate5Tv,climate6Tv;
    //textview
    private TextView type1Tv,type2Tv,type3Tv;
    //456天
    private TextView week4Tv,week5Tv,week6Tv,temperature4Tv,temperature5Tv,temperature6Tv,
            wind4Tv,wind5Tv,wind6Tv,high4Tv,high5Tv,high6Tv,type4Tv,type5Tv,type6Tv;



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

        //为分享按钮单击事件
        share=(ImageView)findViewById(R.id.title_share);

        //为更新按钮添加单击事件
        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);
        progressBar=(ProgressBar)findViewById(R.id.title_update_progress);
        mTitleLocation=(ImageView)findViewById(R.id.title_location);

        mTitleLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MyActivity.this, Locate.class);
                startActivity(i);
            }
        });



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

        //初始化两个滑动页面
        initViews();
        //小圆点初始化
        initDots();
        //初始化控件
        initView();
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDiaog=new ShareDiaog(MyActivity.this);
                shareDiaog.builder().show();
                shareDiaog.setShareClickListener(shareClickListener);
            }
        });

    }


    /**
     * 各平台分享按钮的点击事件
     */
    private ShareDiaog.ShareClickListener shareClickListener=new ShareDiaog.ShareClickListener() {
        @Override
        public void shareWechat() {
            ShareUtils.shareWechat(share_title,share_url,share_desc,share_img,platformActionListener);
        }

        @Override
        public void shareFriend() {
            ShareUtils.shareWechatMoments(share_title,share_url,share_desc,share_img,platformActionListener);

        }

        @Override
        public void shareQQ() {
            ShareUtils.shareQQ(share_title,share_url,share_desc,share_img,platformActionListener);

        }

        @Override
        public void shareQzone() {
            ShareUtils.shareQZone(share_title,share_url,share_desc,share_img,platformActionListener);


        }

        @Override
        public void shareSinaWeibo() {
            ShareUtils.shareSinaWeibo(share_title,share_url,share_desc,share_img,platformActionListener);
        }

        @Override
        public void shareFaceBook() {
            ShareUtils.shareFaceBook(share_title,share_url,share_desc,share_img,platformActionListener);

        }

        @Override
        public void shareTecent() {
            ShareUtils.shareTencentWeibo(share_title,share_url,share_desc,share_img,platformActionListener);

        }

        @Override
        public void shareTwitter() {
            ShareUtils.shareTwitter(share_title,share_url,share_desc,share_img,platformActionListener);

        }


    };

    /**
     * 分享回调
     */
        PlatformActionListener platformActionListener=new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            Log.e("ks","分享成功");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            Log.e("ks","分享失败");

        }

        @Override
        public void onCancel(Platform platform, int i) {
            Log.e("ks","分享取消");

        }
    };
    //初始化小圆点
    private void initDots() {
        dots1=new ImageView[views1.size()];
        for(int i=0;i<views1.size();i++){
            dots1[i]=(ImageView)findViewById(ids[i]);
        }

    }
    //六天天气信息展示
    private void initViews() {
        LayoutInflater inflater=LayoutInflater.from(this);
        views1=new ArrayList<View>();
        views1.add(inflater.inflate(R.layout.futher1,null));
        views1.add(inflater.inflate(R.layout.futher2,null));
        vpAdapter=new ViewPagerAdapter(views1,this);
        vp=(ViewPager)findViewById(R.id.viewpage2);
        vp.setAdapter(vpAdapter);
        //为pageviewer配置监听事件
        vp.setOnPageChangeListener(this);
    }
    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        for(int a=0;a<ids.length;a++){
            if(a==i){
                dots1[a].setImageResource(R.drawable.foucs);
            }else{
                dots1[a].setImageResource(R.drawable.unfoucs);
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {

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
        int weatherTypeCount=0;

        try{
            XmlPullParserFactory fac=XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser=fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType=xmlPullParser.getEventType();
            Log.d("ksningWeather111","parseXML");
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
                            Log.d("ss",xmlPullParser.getName());
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
                        } else if (xmlPullParser.getName().equals("type") && weatherTypeCount == 0) {
                            eventType = xmlPullParser.next();
                            todayWeather.setType(xmlPullParser.getText());
                            weatherTypeCount++;



                        } else if(xmlPullParser.getName().equals("date")&& dateCount==1){
                            Log.d("kssss",xmlPullParser.getName());
                            eventType=xmlPullParser.next();
                            todayWeather.setDate1(xmlPullParser.getText());
                            dateCount++;
                        } else if(xmlPullParser.getName().equals("date")&& dateCount==2){
                            eventType=xmlPullParser.next();
                            todayWeather.setDate2(xmlPullParser.getText());
                            dateCount++;

                        }else if(xmlPullParser.getName().equals("date")&& dateCount==3){
                            eventType=xmlPullParser.next();
                            todayWeather.setDate3(xmlPullParser.getText());
                            dateCount++;
                        }else if(xmlPullParser.getName().equals("date")&& dateCount==4){
                            eventType=xmlPullParser.next();
                            todayWeather.setDate4(xmlPullParser.getText());
                            dateCount++;
                        }else if(xmlPullParser.getName().equals("date")&& dateCount==5){
                            eventType=xmlPullParser.next();
                            todayWeather.setDate5(xmlPullParser.getText());
                            dateCount++;
                        }else if(xmlPullParser.getName().equals("date")&& dateCount==6){
                            eventType=xmlPullParser.next();
                            todayWeather.setDate6(xmlPullParser.getText());
                            dateCount++;
                        }else if(xmlPullParser.getName().equals("low")&& lowCount==1) {
                            eventType = xmlPullParser.next();
                            todayWeather.setLow1(xmlPullParser.getText());
                            lowCount++;

                        }else if(xmlPullParser.getName().equals("low")&& lowCount==2) {
                            eventType = xmlPullParser.next();
                            todayWeather.setLow2(xmlPullParser.getText());
                            lowCount++;
                        }else if(xmlPullParser.getName().equals("low")&& lowCount==3) {
                            eventType = xmlPullParser.next();
                            todayWeather.setLow3(xmlPullParser.getText());
                            lowCount++;
                        }else if(xmlPullParser.getName().equals("low")&& lowCount==4) {
                            eventType = xmlPullParser.next();
                            todayWeather.setLow4(xmlPullParser.getText());
                            lowCount++;
                        }else if(xmlPullParser.getName().equals("low")&& lowCount==5) {
                            eventType = xmlPullParser.next();
                            todayWeather.setLow5(xmlPullParser.getText());
                            lowCount++;
                        }else if(xmlPullParser.getName().equals("low")&& lowCount==6) {
                            eventType = xmlPullParser.next();
                            todayWeather.setLow6(xmlPullParser.getText());
                            lowCount++;

                        }else if(xmlPullParser.getName().equals("high")&&highCount==1){
                            eventType=xmlPullParser.next();
                            todayWeather.setHigh1(xmlPullParser.getText());
                            highCount++;
                        }else if(xmlPullParser.getName().equals("high")&&highCount==2){
                            eventType=xmlPullParser.next();
                            todayWeather.setHigh2(xmlPullParser.getText());
                            highCount++;
                        }else if(xmlPullParser.getName().equals("high")&&highCount==3){
                            eventType=xmlPullParser.next();
                            todayWeather.setHigh3(xmlPullParser.getText());
                            highCount++;
                        }else if(xmlPullParser.getName().equals("high")&&highCount==4){
                            eventType=xmlPullParser.next();
                            todayWeather.setHigh4(xmlPullParser.getText());
                            highCount++;
                        }else if(xmlPullParser.getName().equals("high")&&highCount==5){
                            eventType=xmlPullParser.next();
                            todayWeather.setHigh5(xmlPullParser.getText());
                            highCount++;
                        }else if(xmlPullParser.getName().equals("high")&&highCount==6){
                            eventType=xmlPullParser.next();
                            todayWeather.setHigh6(xmlPullParser.getText());
                            highCount++;
                        }else if(xmlPullParser.getName().equals("fengli")&&fengliCount==1){
                            eventType=xmlPullParser.next();
                            todayWeather.setFengli1(xmlPullParser.getText());
                            fengliCount++;
                        }else if(xmlPullParser.getName().equals("fengli")&&fengliCount==2){
                            eventType=xmlPullParser.next();
                            todayWeather.setFengli2(xmlPullParser.getText());
                            fengliCount++;
                        }else if(xmlPullParser.getName().equals("fengli")&&fengliCount==3){
                            eventType=xmlPullParser.next();
                            todayWeather.setFengli3(xmlPullParser.getText());
                            fengliCount++;
                        }else if(xmlPullParser.getName().equals("fengli")&&fengliCount==4){
                            eventType=xmlPullParser.next();
                            todayWeather.setFengli4(xmlPullParser.getText());
                            fengliCount++;
                        }else if(xmlPullParser.getName().equals("fengli")&&fengliCount==5){
                            eventType=xmlPullParser.next();
                            todayWeather.setFengli5(xmlPullParser.getText());
                            fengliCount++;
                        }else if(xmlPullParser.getName().equals("fengli")&&fengliCount==6){
                            eventType=xmlPullParser.next();
                            todayWeather.setFengli6(xmlPullParser.getText());
                            fengliCount++;

                        }else if(xmlPullParser.getName().equals("type")&&weatherTypeCount==1){
                            eventType=xmlPullParser.next();
                            todayWeather.setWeatherType1(xmlPullParser.getText());
                            weatherTypeCount++;
                        }else if(xmlPullParser.getName().equals("type")&&weatherTypeCount==2){
                            eventType=xmlPullParser.next();
                            todayWeather.setWeatherType2(xmlPullParser.getText());
                            weatherTypeCount++;
                        }else if(xmlPullParser.getName().equals("type")&&weatherTypeCount==3){
                            eventType=xmlPullParser.next();
                            todayWeather.setWeatherType3(xmlPullParser.getText());
                            weatherTypeCount++;
                        }else if(xmlPullParser.getName().equals("type")&&weatherTypeCount==4){
                            eventType=xmlPullParser.next();
                            todayWeather.setWeatherType4(xmlPullParser.getText());
                            weatherTypeCount++;
                        }else if(xmlPullParser.getName().equals("type")&&weatherTypeCount==5){
                            eventType=xmlPullParser.next();
                            todayWeather.setWeatherType5(xmlPullParser.getText());
                            weatherTypeCount++;
                        }else if(xmlPullParser.getName().equals("type")&&weatherTypeCount==6){
                            eventType=xmlPullParser.next();
                            todayWeather.setWeatherType6(xmlPullParser.getText());
                            weatherTypeCount++;
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

        week1Tv=views1.get(0).findViewById(R.id.firstTime);
        week2Tv=views1.get(0).findViewById(R.id.secondTime);
        week3Tv=views1.get(0).findViewById(R.id.thirdTime);
        week4Tv=views1.get(1).findViewById(R.id.fourTime);
        week5Tv=views1.get(1).findViewById(R.id.fiveTime);
        week6Tv=views1.get(1).findViewById(R.id.sixTime);
        temperature1Tv=views1.get(0).findViewById(R.id.firstTemperature);
        temperature2Tv=views1.get(0).findViewById(R.id.secondTemperature);
        temperature3Tv=views1.get(0).findViewById(R.id.thirdTemperature);
        temperature4Tv=views1.get(1).findViewById(R.id.fourTemperature);
        temperature5Tv=views1.get(1).findViewById(R.id.fiveTemperature);
        temperature6Tv=views1.get(1).findViewById(R.id.sixTemperature);
        high1Tv=views1.get(0).findViewById(R.id.firstHigh);
        high2Tv=views1.get(0).findViewById(R.id.secondHigh);
        high3Tv=views1.get(0).findViewById(R.id.thirdHigh);
        high4Tv=views1.get(1).findViewById(R.id.fourHigh);
        high5Tv=views1.get(1).findViewById(R.id.fiveHigh);
        high6Tv=views1.get(1).findViewById(R.id.sixHigh);
        wind1Tv=views1.get(0).findViewById(R.id.firstWind);
        wind2Tv=views1.get(0).findViewById(R.id.secondWind);
        wind3Tv=views1.get(0).findViewById(R.id.thirdWind);
        wind4Tv=views1.get(1).findViewById(R.id.fourWind);
        wind5Tv=views1.get(1).findViewById(R.id.fiveWind);
        wind6Tv=views1.get(1).findViewById(R.id.sixWind);
        type1Tv=views1.get(0).findViewById(R.id.firstType);
        type2Tv=views1.get(0).findViewById(R.id.secondType);
        type3Tv=views1.get(0).findViewById(R.id.thirdType);
        type4Tv=views1.get(1).findViewById(R.id.fourType);
        type5Tv=views1.get(1).findViewById(R.id.fiveType);
        type6Tv=views1.get(1).findViewById(R.id.sixType);
        climate1Tv=views1.get(0).findViewById(R.id.firstWeatherState);
        climate2Tv=views1.get(0).findViewById(R.id.secondWeatherState);
        climate3Tv=views1.get(0).findViewById(R.id.thirdWeatherState);
        climate4Tv=views1.get(1).findViewById(R.id.fourWeatherState);
        climate5Tv=views1.get(1).findViewById(R.id.fiveWeatherState);
        climate6Tv=views1.get(1).findViewById(R.id.sixWeatherState);



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


        //未来三天天气

        week1Tv.setText("N/A");
        week2Tv.setText("N/A");
        week3Tv.setText("N/A");
        week4Tv.setText("N/A");
        week5Tv.setText("N/A");
        week6Tv.setText("N/A");
        temperature1Tv.setText("N/A");
        temperature2Tv.setText("N/A");
        temperature3Tv.setText("N/A");
        temperature4Tv.setText("N/A");
        temperature5Tv.setText("N/A");
        temperature6Tv.setText("N/A");

        wind2Tv.setText("N/A");
        wind3Tv.setText("N/A");
        wind1Tv.setText("N/A");
        wind4Tv.setText("N/A");
        wind5Tv.setText("N/A");
        wind6Tv.setText("N/A");

        high1Tv.setText("N/A");
        high2Tv.setText("N/A");
        high3Tv.setText("N/A");
        high4Tv.setText("N/A");
        high5Tv.setText("N/A");
        high6Tv.setText("N/A");
        type1Tv.setText("N/A");
        type2Tv.setText("N/A");
        type3Tv.setText("N/A");
        type4Tv.setText("N/A");
        type5Tv.setText("N/A");
        type6Tv.setText("N/A");




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
        if(todayWeather.getPm25()!=null) pmDataTv.setText(todayWeather.getPm25());
        else pmDataTv.setText("无");
        if(todayWeather.getQuality()!=null) pmQualityTv.setText(todayWeather.getQuality());
        else pmQualityTv.setText("无");
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText("温度:"+todayWeather.getWendu());
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力:"+todayWeather.getFengli());
        temperatureRangeTv.setText(todayWeather.getLow()+"~"+todayWeather.getHigh());

        //未来六天的值
        Log.d("33",todayWeather.getWeatherType5());
        week1Tv.setText(todayWeather.getDate1());
        week2Tv.setText(todayWeather.getDate2());
        week3Tv.setText(todayWeather.getDate3());
        week4Tv.setText(todayWeather.getDate4());
        week5Tv.setText(todayWeather.getDate5());
        week6Tv.setText(todayWeather.getDate6());

        temperature1Tv.setText(todayWeather.getLow1());
        temperature2Tv.setText(todayWeather.getLow2());
        temperature3Tv.setText(todayWeather.getLow3());
        temperature4Tv.setText(todayWeather.getLow4());
        temperature5Tv.setText(todayWeather.getLow5());
        temperature6Tv.setText(todayWeather.getLow6());

        high1Tv.setText(todayWeather.getHigh1());
        high2Tv.setText(todayWeather.getHigh2());
        high3Tv.setText(todayWeather.getHigh3());
        high4Tv.setText(todayWeather.getHigh4());
        high5Tv.setText(todayWeather.getHigh5());
        high6Tv.setText(todayWeather.getHigh6());


        wind1Tv.setText(todayWeather.getFengli1());
        wind2Tv.setText(todayWeather.getFengli2());
        wind3Tv.setText(todayWeather.getFengli3());
        wind4Tv.setText(todayWeather.getFengli4());
        wind5Tv.setText(todayWeather.getFengli5());
        wind6Tv.setText(todayWeather.getFengli6());

        type1Tv.setText(todayWeather.getWeatherType1());
        type2Tv.setText(todayWeather.getWeatherType2());
        type3Tv.setText(todayWeather.getWeatherType3());
        type4Tv.setText(todayWeather.getWeatherType4());
        type5Tv.setText(todayWeather.getWeatherType5());
        type6Tv.setText(todayWeather.getWeatherType6());

       //根据解析的pm2.5的值来更新pm2.5图案

       if(todayWeather.getPm25()!=null){
           int pm25=Integer.parseInt(todayWeather.getPm25());
           if(pm25<=50) pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
           if(pm25>50&&pm25<=100) pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);
           if(pm25>100&&pm25<=150) pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);
           if(pm25>150&&pm25<=200) pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
           if(pm25>200&&pm25<=300) pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
           if(pm25>300) pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);

       }
        //根据解析出来的天气情况来转换成相应的天气图片

        String climate=todayWeather.getType();
        String climate1=todayWeather.getWeatherType1();
        String climate2=todayWeather.getWeatherType2();
        String climate3=todayWeather.getWeatherType3();
        String climate4=todayWeather.getWeatherType4();
        String climate5=todayWeather.getWeatherType5();
        String climate6=todayWeather.getWeatherType6();

        set(climate,weatherImg);
        set(climate1,climate1Tv);
        set(climate2,climate2Tv);
        set(climate3,climate3Tv);
        set(climate4,climate4Tv);
        set(climate5,climate5Tv);
        set(climate6,climate6Tv);







        Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();





    }
    public void set(String climate,ImageView weatherImg){
        if(climate.equals("暴雪"))
            weatherImg.setImageResource(R.mipmap.biz_plugin_weather_baoxue);
        if(climate.equals("暴雨"))
            weatherImg.setImageResource(R.mipmap.biz_plugin_weather_baoyu);
        if(climate.equals("大暴雨"))
            weatherImg.setImageResource(R.mipmap.biz_plugin_weather_dabaoyu);
        if(climate.equals("大雪"))
            weatherImg.setImageResource(R.mipmap.biz_plugin_weather_daxue);
        if(climate.equals("大雨"))
            weatherImg.setImageResource(R.mipmap.biz_plugin_weather_dayu);
        if(climate.equals("多云"))
            weatherImg.setImageResource(R.mipmap.biz_plugin_weather_duoyun);
        if(climate.equals("阵雨"))
            weatherImg.setImageResource(R.mipmap.biz_plugin_weather_zhenyu);
        if(climate.equals("阵雪"))
            weatherImg.setImageResource(R.mipmap.biz_plugin_weather_zhenxue);
        if(climate.equals("雷阵雨"))
            weatherImg.setImageResource(R.mipmap.biz_plugin_weather_leizhenyu);
        if(climate.equals("雷阵雨冰雹"))
            weatherImg.setImageResource(R.mipmap.biz_plugin_weather_leizhenyubingbao);
        if(climate.equals("晴"))
            weatherImg.setImageResource(R.mipmap.biz_plugin_weather_qing);
        if(climate.equals("阴"))
            weatherImg.setImageResource(R.mipmap.biz_plugin_weather_yin);
        if(climate.equals("沙尘暴"))
            weatherImg.setImageResource(R.mipmap.biz_plugin_weather_shachenbao);
        if(climate.equals("特大暴雨"))
            weatherImg.setImageResource(R.mipmap.biz_plugin_weather_tedabaoyu);
        if(climate.equals("雾"))
            weatherImg.setImageResource(R.mipmap.biz_plugin_weather_wu);
        if(climate.equals("小雨"))
            weatherImg.setImageResource(R.mipmap.biz_plugin_weather_xiaoyu);
        if(climate.equals("小雪"))
            weatherImg.setImageResource(R.mipmap.biz_plugin_weather_xiaoxue);
        if(climate.equals("中雪"))
            weatherImg.setImageResource(R.mipmap.biz_plugin_weather_zhongxue);
        if(climate.equals("中雨"))
            weatherImg.setImageResource(R.mipmap.biz_plugin_weather_zhongyu);
        if(climate.equals("雨夹雪"))
            weatherImg.setImageResource(R.mipmap.biz_plugin_weather_yujiaxue);



    }



}
