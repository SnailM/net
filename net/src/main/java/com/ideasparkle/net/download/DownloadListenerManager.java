package com.ideasparkle.net.download;

import android.text.TextUtils;
import android.util.Log;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Response;

public enum DownloadListenerManager {
    Instance;
    public static final String DOWN_LISTENER_HEAD = "ChenslDownListener";
    private static final String TAG = "DownloadListenerManager";

    private Map<String, IDownloadListener> downloadMap;

    public Interceptor getInterceptor() {
        return chain -> wrapResponseBody(chain.proceed(chain.request()));
    }

    public void addListener(String key, IDownloadListener IDownloadListener) {
        if (downloadMap == null) {
            downloadMap = new LinkedHashMap<>();
        }

        Log.d(TAG, "addListener() called with: key = [" + key + "], IDownloadListener = [" + IDownloadListener + "]");

        if (!downloadMap.containsKey(key)) {
            downloadMap.put(key, IDownloadListener);
            Log.d(TAG, "addListener: add success .");
        } else {
            Log.e(TAG, "addListener: key [ " + key + " ] is exist .");
        }
    }

    private Response wrapResponseBody(Response response) {
        if (response == null) {
            return null;
        }
        String downHead = response.request().header(DOWN_LISTENER_HEAD);
        if (TextUtils.isEmpty(downHead) || !downloadMap.containsKey(downHead)) {
            return response;
        }
        if (response.body() == null) {
            return response;
        }
        IDownloadListener downloadListener = downloadMap.get(downHead);
        return response.newBuilder()
                .body(new WrapResponseBody(response.body(), 500, downHead, downloadListener))
                .build();
    }
}
