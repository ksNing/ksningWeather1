package com.example.ks.ksningweather.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ks.ksningweather.MyActivity;
import com.example.ks.ksningweather.R;


public class Login extends Activity implements View.OnClickListener {
    private Button loginBtn;
    private EditText userIdEt;
    private EditText passEt;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        loginBtn=(Button)findViewById(R.id.login_button);
        loginBtn.setOnClickListener(this);
        userIdEt=(EditText)findViewById(R.id.name);
        passEt=(EditText)findViewById(R.id.password);

    }

    @Override
    public void onClick(View v) {
        String userName=userIdEt.getText().toString().trim();
        String passWord=passEt.getText().toString().trim();
        if(userName.equals("")){
             Toast.makeText(Login.this, "用户名不允许为空", Toast.LENGTH_LONG).show();
             return;
        }
        if(passWord.equals("")){
             Toast.makeText(Login.this, "密码不允许为空", Toast.LENGTH_LONG).show();
             return;
        }
        if(userName.equals("admin")&&passWord.equals("0000")){
            Toast.makeText(Login.this, "成功登录", Toast.LENGTH_LONG).show();
            Intent i=new Intent(this, MyActivity.class);
            startActivity(i);
            Login.this.finish();
        }else{
            Toast.makeText(Login.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
        }

    }
}
