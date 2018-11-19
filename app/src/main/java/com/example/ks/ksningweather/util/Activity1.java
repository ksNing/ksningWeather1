package com.example.ks.ksningweather.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.ks.ksningweather.R;

public class Activity1 extends Activity {
    private static final String TAG = "ks";
    private Button btn;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        Log.i(TAG, "The activity1 state--------onCreate");
        setContentView(R.layout.ks);
        btn = (Button) findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity1.this, Activity2.class);
                startActivity(i);
            }
        });
    }
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "The activity1 state--------onStart");

    }
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "The activity1 state--------onRestart");
    }
    //重新开始
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "The activity1 state--------onResume");
    }
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "The activity1 state--------onPause");
        if (isFinishing()) {
            Log.w(TAG, "The activity1 will be destroyed!");
        } else {
            Log.w(TAG, "The activity1 is just pausing!");
        }
    }
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "The activity1 state--------onStop");
    }
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "The activity1 state--------onDestory");
    }

}
