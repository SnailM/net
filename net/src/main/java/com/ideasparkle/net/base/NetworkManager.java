package com.ideasparkle.net.base;

import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class NetworkManager {
    private static final NetworkManager instance = new NetworkManager();

    public static final ObservableTransformer IO_TRANSFORMER = upstream -> upstream.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    private static final String TAG = "NetworkManager";
    //默认调度线程
    private Scheduler defaultScheduler;
    //请求列表管理
    private HashMap<String, Disposable> requestList = new HashMap<>();

    public static NetworkManager getInstance() {
        return instance;
    }

    public static <T> ObservableTransformer<T, T> applySchedulers(ObservableTransformer transformer) {
        //noinspection unchecked
        return (ObservableTransformer<T, T>) transformer;
    }

    public Scheduler getScheduler() {
        if (defaultScheduler == null) {
            defaultScheduler = Schedulers.io();
        }
        return defaultScheduler;
    }

    /**
     * 添加请求
     *
     * @param tag          tag
     * @param subscription 具体请求操作
     */
    public void addRequest(String tag, Disposable subscription) {
        synchronized (this) {
            if (requestList.containsKey(tag)) {
                cancelRequest(tag);
            }
            requestList.put(tag, subscription);
        }
        monitorsRequest();
    }

    /**
     * 取消请求
     *
     * @param tag tag
     */
    public void cancelRequest(String tag) {
        if (requestList.containsKey(tag)) {
            if (!requestList.get(tag).isDisposed()) {
                requestList.get(tag).dispose();
            }
            requestList.remove(tag);
        }
        monitorsRequest();
    }

    /**
     * 取消所有任务
     */
    public void cancelAllRequest() {
        Set<Map.Entry<String, Disposable>> set = requestList.entrySet();
        Iterator<Map.Entry<String, Disposable>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Disposable> subscriptionEntry = iterator.next();
            if (!subscriptionEntry.getValue().isDisposed()) {
                subscriptionEntry.getValue().dispose();
            }
            iterator.remove();
        }
//        monitorsRequest();
    }

    /**
     * 请求队列变化的日志
     */
    private void monitorsRequest() {
        Set<String> keys_tag = requestList.keySet();
        StringBuilder stringBuilder = new StringBuilder("");
        for (String tag : keys_tag) {
            stringBuilder.append(tag).append("-->\n");
        }
        Log.d(TAG, "monitorsRequestStatus:\n" + stringBuilder.toString());
    }

    public Action getCompleteAction(final String tag) {
        return () -> cancelRequest(tag);
    }

    public <T> T create(String baseUrl, Class<T> service) {
        if (OkhttpClientHelper.getInstance().getClient() == null) {
            OkhttpClientHelper.getInstance().init();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(OkhttpClientHelper.getInstance().getClient())
                .addConverterFactory(new XmlOrJsonConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(service);
    }
}
