package com.example.ks.ksningweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ks.ksningweather.Bean.City;
import com.example.ks.ksningweather.app.MyApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.transform.Result;

public class SelectCity extends Activity implements View.OnClickListener {

    private ImageView mBackBtn;
    private ListView mList;
    private List<City> mCityList;
    private String updateCityCode="-1";
    private ArrayList<String> mArrayList;

    private EditText searchText;
    private ImageView searchBtn;



    @Override
    protected void onCreate(Bundle savedIntanceState) {
        super.onCreate(savedIntanceState);

        setContentView(R.layout.select_city);


        //为返回键添加点击响应事件
        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);

        //为搜索添加响应事件
        searchText = (EditText) findViewById(R.id.selectcity_search);
        searchBtn = (ImageView) findViewById(R.id.selectcity_search_button);
        searchBtn.setOnClickListener(this);

        MyApplication myApplication = (MyApplication) getApplication();
        mCityList = myApplication.getCityList();
        mArrayList=new ArrayList<String>();
        int n=mCityList.size();
        String [] data1=new String[n];
        String [] data=new String[n];
        for(int i=0;i<n;i++){
            String name=mCityList.get(i).getCity();
            String number=mCityList.get(i).getNumber();
            mArrayList.add(name+" "+number);

        }


        mList = (ListView) findViewById(R.id.title_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                SelectCity.this, android.R.layout.simple_list_item_1, mArrayList);
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateCityCode = mCityList.get(position).getNumber();
                Log.d("update city code:",updateCityCode);
                Toast.makeText(SelectCity.this, "你单击了:" +position,
                        Toast.LENGTH_SHORT).show();
            }


        });


    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.title_back:
                Intent i=new Intent(this,MyActivity.class);
                i.putExtra("cityCode",updateCityCode);
                setResult(RESULT_OK, i);
                finish();
                break;
            case R.id.selectcity_search_button:
                String cityCode = searchText.getText().toString();
                Log.d("ks2:",cityCode);
                Intent intent=new Intent(this,MyActivity.class);
                intent.putExtra("cityCode",cityCode);
                setResult(RESULT_OK,intent);
                finish();
                break;
            default:
                break;
        }

    }
}



