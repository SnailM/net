package com.ideasparkle.net.base;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

public class ResponseTransformer {
    public static <T> ObservableTransformer<BaseResponse<T>, T> handleResult() {
        return upstream -> upstream
                .onErrorResumeNext(new ErrorResumeFunction<>())
                .flatMap(new ResponseFunction<>());
    }

    /**
     * 非服务器产生的异常，比如本地无无网络请求，Json数据解析错误等等。
     *
     * @param <T>
     */
    private static class ErrorResumeFunction<T> implements Function<Throwable, ObservableSource<? extends BaseResponse<T>>> {
        @Override
        public ObservableSource<? extends BaseResponse<T>> apply(Throwable throwable) throws Exception {
            return Observable.error(CustomException.handleException(throwable));
        }
    }

    /**
     * 服务其返回的数据解析
     * 正常服务器返回数据和服务器可能返回的exception
     *
     * @param <T>
     */
    private static class ResponseFunction<T> implements Function<BaseResponse<T>, ObservableSource<T>> {
        @Override
        public ObservableSource<T> apply(BaseResponse<T> tResponse) throws Exception {
//            MyApplication.timestamp = tResponse.getTimestamp();
            int code = tResponse.getCode();
            String message = tResponse.getMessage();
            if (code == 200) {
                if (tResponse.getResult() != null) {
                    return Observable.just(tResponse.getResult());
                }
            } else if (code == 10004) {
                //退出登录
//                LoginActivity.start(MyApplication.getInstance(), "ResponseFunction");
//                ToastUtil.showToast("用户信息已过期，请重新登录~");
                return Observable.error(new ApiException(code, ""));
            }
            return Observable.error(new ApiException(code, message));
        }
    }
}