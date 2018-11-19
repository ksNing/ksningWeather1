package com.example.ks.ksningweather.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ks.ksningweather.R;

public class Activity2 extends Activity {
    private Button btn2;
    private static final String TAG = "ks";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "The activity2 state--------onCreate");
        setContentView(R.layout.ks1);

        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity2.this, Activity1.class);
                startActivity(i);
            }
        });
    }
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "The activity2 state--------onStart");

    }
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "The activity2 state--------onRestart");
    }
    //重新开始
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "The activity2 state--------onResume");
    }
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "The activity2 state--------onPause");
        if (isFinishing()) {
            Log.w(TAG, "The activity2 will be destroyed!");
        } else {
            Log.w(TAG, "The activity2 is just pausing!");
        }
    }
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "The activity2 state--------onStop");
    }
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "The activity2 state--------onDestory");
    }

}
