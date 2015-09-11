package com.yksong.simplepx.model;

import java.util.List;

/**
 * Created by esong on 15-09-08.
 */
public class ApiResult {
    public int current_page = 0;
    public int total_pages;
    public List<Photo> photos;
    public String feature;

    public ApiResult(String feature) {
        this.feature = feature;
    }
}
