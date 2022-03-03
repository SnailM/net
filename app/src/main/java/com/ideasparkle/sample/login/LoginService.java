package com.ideasparkle.sample.login;

import com.ideasparkle.net.base.BaseResponse;
import com.ideasparkle.net.base.ConvertType;
import com.ideasparkle.net.base.OkhttpClientHelper;
import com.ideasparkle.sample.login.bean.BootResp;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface LoginService {
    class Factory {
        public static LoginService create() {
            return OkhttpClientHelper.getInstance().getRetrofit().create(LoginService.class);
        }
    }

    @GET
    @ConvertType.Json
    Observable<BaseResponse<BootResp>> getBoot(@Url String url, @Query("channelCode") String channelId);
}
