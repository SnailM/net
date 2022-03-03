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
        testHttpRequest();
    }

    private void testHttpRequest() {
        LoginModel loginModel = new LoginModel();
        loginModel.getBoot("getBoot",
                "https://hcapihongdaotech.bestv.com.cn/api/device/boot/serverConfig",
                "YQX_HOMESCHOOL_20191021",
                new IBaseCallback<BootResp>() {
                    @Override
                    public void onResult(BootResp result) {
                        Log.d("mxj", "success " + result.getServerConfigList().get(0).getServerKey());
                    }

                    @Override
                    public void onError(ApiException throwable) {
                        Log.d("mxj", "error " + throwable.getMessage());
                    }
                });
    }
}