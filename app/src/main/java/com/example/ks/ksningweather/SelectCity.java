package com.example.ks.ksningweather;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ks.ksningweather.Bean.City;
import com.example.ks.ksningweather.app.MyApplication;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.xml.transform.Result;

public class SelectCity extends Activity implements View.OnClickListener {

    private ImageView mBackBtn;
    private ListView mList;
    private List<City> mCityList;
    private String updateCityCode="-1";
    private ArrayList<String> mArrayList;
    private AutoCompleteTextView autoComplete;
    private Button button;
    private static String []data ={"aaa","bbb","ccc"};
    private  ArrayAdapter<String> adapter;
    private ArrayList<String> mArray1List;
    private EditText editText;
    private List<City> filterDateList;



    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedIntanceState) {
        super.onCreate(savedIntanceState);

        setContentView(R.layout.select_city);


        //为返回键添加点击响应事件
        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);

        //自动搜素框控件
        autoComplete=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        button=(Button)findViewById(R.id.button);

        editText=(EditText)findViewById(R.id.select_search);





        //展示城市列表的准备
        MyApplication myApplication = (MyApplication) getApplication();
        mCityList = myApplication.getCityList();
        mArrayList=new ArrayList<String>();
        mArray1List=new ArrayList<String>();
        int n=mCityList.size();
        //String [] data1=new String[n];
        //String [] data=new String[n];
        for(int i=0;i<n;i++){
            String name=mCityList.get(i).getCity();
            String number=mCityList.get(i).getNumber();
            String province=mCityList.get(i).getProvince();
            String py=mCityList.get(i).getAllPY();
            mArrayList.add(name);
            mArray1List.add(mCityList.get(i).getAllPY().toLowerCase()+mCityList.get(i).getCity());
        }
        Collator cmp = Collator.getInstance(Locale.CHINA);
        Arrays.sort(new ArrayList[]{mArrayList},cmp);
        Log.d("1111111", String.valueOf(mArrayList));

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                SelectCity.this, android.R.layout.simple_dropdown_item_1line, mArray1List);
        //adapter1.setNotifyOnChange(true);
        editText.addTextChangedListener(new TextWatcher() {
            // 输入文本之前的状态
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            // 输入文本中的状态
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               
            }

            // 输入文本之后的状态
            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        mList = (ListView) findViewById(R.id.title_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                SelectCity.this, android.R.layout.simple_list_item_1, mArrayList);

        mList.setAdapter(adapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateCityCode = mCityList.get(position).getNumber();
                Log.d("update city code:",updateCityCode);

                //用sharedPerferenced来存储最近的一次citycode
                SharedPreferences sharedPreferences=getSharedPreferences("cityCodePreferenced",Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("cityCode",updateCityCode);
                editor.commit();

                Toast.makeText(SelectCity.this,"你单击了:" +position,
                        Toast.LENGTH_SHORT).show();
                Intent i=new Intent();
                i.putExtra("cityCode",updateCityCode);
                setResult(RESULT_OK, i);
                finish();

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

            default:
                break;
        }

    }
    private void filterData(String filterStr){
        filterDateList=new ArrayList<City>();

        if(TextUtils.isEmpty(filterStr)){
            for(City city:mCityList){

                filterDateList.add(city);
            }
        }else{
            filterDateList.clear();
            for(City city:mCityList){
                if((city.getCity().contains(filterStr.toString()))||city.getAllPY().contains(filterStr.toString().toUpperCase())
                        ||city.getAllFirstPY().contains(filterStr.toString().toUpperCase())){
                    filterDateList.add(city);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

}



