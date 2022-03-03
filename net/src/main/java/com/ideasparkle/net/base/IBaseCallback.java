package com.ideasparkle.net.base;

public abstract class IBaseCallback<T> {
    public abstract void onResult(T result);

    public abstract void onError(ApiException throwable);

    public void onNotApiError(Throwable throwable) {
        throwable.printStackTrace();
    }

    public void onError(Throwable throwable) {
        if (throwable instanceof ApiException) {
            onError((ApiException)throwable);
        } else {
            onNotApiError(throwable);
        }
    }
}
