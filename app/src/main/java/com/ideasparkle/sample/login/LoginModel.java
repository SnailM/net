package com.ideasparkle.sample.login;

import com.ideasparkle.net.base.ApiException;
import com.ideasparkle.net.base.BaseModel;
import com.ideasparkle.net.base.BaseResponse;
import com.ideasparkle.net.base.IBaseCallback;
import com.ideasparkle.net.base.NetworkManager;
import com.ideasparkle.net.base.ResponseTransformer;
import com.ideasparkle.sample.login.bean.BootResp;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class LoginModel extends BaseModel {
    public LoginModel() {
        super();
    }

    public void getBoot(String tag, String url, String channel, IBaseCallback<BootResp> callback) {
        Observable<BaseResponse<BootResp>> observable =
                RequestServices.getInstance().getLoginService().getBoot(url, channel);

        Disposable disposable = observable
                .compose(ResponseTransformer.handleResult())
                .compose(NetworkManager.applySchedulers(NetworkManager.IO_TRANSFORMER))
                .subscribe(callback::onResult, throwable -> {
                    // 处理异常
                    callback.onError((ApiException) throwable);
                });
        mDisposable.add(disposable);
    }
}
