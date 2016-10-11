package com.example.mypc.doubleprocess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //第一次启动
        this.startService(new Intent(MainActivity.this,LocalService.class));
        //第一次启动
        this.startService(new Intent(MainActivity.this,RemoteService.class));
    }
}
