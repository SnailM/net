package com.ideasparkle.net.base;

import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import io.reactivex.annotations.Nullable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class XmlOrJsonConverterFactory extends Converter.Factory {
    private static final String TAG = "XmlOrJsonConverter";

    private final Converter.Factory xml = SimpleXmlConverterFactory.create();
    private final Converter.Factory json = GsonConverterFactory.create();
    private final Converter.Factory string = new StringConverterFactory();

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        Log.d(TAG, "responseBodyConverter type: " + type.toString());
        for (Annotation annotation : annotations) {
            Log.d(TAG, "responseBodyConverter annotation: " + annotation.annotationType());
            if (annotation.annotationType() == ConvertType.Xml.class) {
                return xml.responseBodyConverter(type, annotations, retrofit);
            } else if (annotation.annotationType() == ConvertType.Json.class) {
                return json.responseBodyConverter(type, annotations, retrofit);
            } else if (annotation.annotationType() == ConvertType.String.class) {
                return string.responseBodyConverter(type, annotations, retrofit);
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        for (Annotation annotation : parameterAnnotations) {
            Log.d(TAG, "requestBodyConverter: " + annotation.annotationType());
            if (annotation.annotationType() == ConvertType.Json.class) {
                return json.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
            } else if (annotation.annotationType() == ConvertType.Xml.class) {
                return xml.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
            }
        }
        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }
}
