package com.yksong.simplepx.network;

import com.yksong.simplepx.model.ApiResult;
import com.yksong.simplepx.model.Photo;
import com.yksong.simplepx.model.SinglePhotoResult;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by esong on 15-09-08.
 */
public interface PxApi {
    @GET("/photos?image_size=440&rpp=50")
    Observable<ApiResult> photos(@Query("feature") String feature, @Query("page") int page);

    @GET("/photos/{id}?image_size=4")
    Observable<SinglePhotoResult> photoById(@Path("id") int Id);
}