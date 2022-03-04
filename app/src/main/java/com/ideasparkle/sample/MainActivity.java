package com.ideasparkle.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ideasparkle.net.base.ApiException;
import com.ideasparkle.net.base.IBaseCallback;
import com.ideasparkle.sample.login.LoginModel;
import com.ideasparkle.sample.login.bean.BootResp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}