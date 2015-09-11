package com.yksong.simplepx.network;

import com.yksong.simplepx.model.ApiResult;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by esong on 15-09-08.
 */
public interface PxApi {
    @GET("/photos?image_size=440&rpp=50")
    Observable<ApiResult> photos(@Query("feature") String feature);
}