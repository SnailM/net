package com.ideasparkle.net.base;

import android.text.TextUtils;

import com.ideasparkle.net.BuildConfig;
import com.ideasparkle.net.download.DownloadListenerManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class OkhttpClientHelper {
    private static final OkhttpClientHelper instance = new OkhttpClientHelper();
    private OkHttpClient client;
    private Retrofit retrofit;
    public static final String DONT_HEADER_KEY = "DONT_HEADER";

    public static OkhttpClientHelper getInstance() {
        return instance;
    }

    /**
     * 初始化okhttpClient
     */
    public void init() {
        BaseLoggingInterceptor loggingInterceptor = new BaseLoggingInterceptor();
        loggingInterceptor.setLevel(BaseLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addNetworkInterceptor(DownloadListenerManager.Instance.getInterceptor())
                .addInterceptor(chain -> {
                    Request original = chain.request();

                    if (!TextUtils.isEmpty(original.header(DONT_HEADER_KEY))) {
                        Request request = original.newBuilder().removeHeader(DONT_HEADER_KEY).build();
                        return chain.proceed(request);
                    } else {
                        Request request = original.newBuilder()
//                                .header("interface_access_token", SaveValueToShared.getInstance().getStringFromSP(MyApplication.instance, Constants.LOGIN_USER_TOKEN, ""))
//                                .header("device_info", DeviceUtil.getHttpHeaderInfo())
//                                .header("stdid", LoginInfoUtils.getCurChildId())
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    }
                })
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor);
        }
        client = builder.build();
    }

    public OkHttpClient getClient() {
        return client;
    }

    public Retrofit getRetrofit() {
        if (client == null) {
            init();
        }
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BaseHttpUrl.baseUrl)
                    .client(client)
                    .addConverterFactory(new XmlOrJsonConverterFactory())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public Retrofit getRetrofit(String baseUrl) {
        if (client == null) {
            init();
        }
        if (retrofit == null) {
            if (TextUtils.isEmpty(baseUrl)) {
                baseUrl = BaseHttpUrl.baseUrl;
            }
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(new XmlOrJsonConverterFactory())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public <T> T getRetrofit(String baseUrl, Class<T> service) {
        if (client == null) {
            init();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(new XmlOrJsonConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(service);
    }
}
