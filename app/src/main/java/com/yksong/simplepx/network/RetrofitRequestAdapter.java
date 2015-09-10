package com.yksong.simplepx.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import oauth.signpost.http.HttpRequest;
import retrofit.client.Header;
import retrofit.client.Request;

/**
 * Created by esong on 15-09-08.
 */
public class RetrofitRequestAdapter implements HttpRequest {
    private static final String DEFAULT_CONTENT_TYPE = "application/json";

    private Request request;

    private String contentType;

    public RetrofitRequestAdapter(Request request) {
        this(request, request.getBody() != null ? request.getBody().mimeType() : DEFAULT_CONTENT_TYPE);
    }

    public RetrofitRequestAdapter(Request request, String contentType) {
        this.request = request;
        this.contentType = contentType;
    }

    @Override
    public Map<String, String> getAllHeaders() {
        HashMap<String, String> headers = new HashMap<String, String>();
        for(Header header : request.getHeaders()) {
            headers.put(header.getName(), header.getValue());
        }
        return headers;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getHeader(String key) {
        for(Header header : request.getHeaders()) {
            if(key.equals(header.getName())) {
                return header.getValue();
            }
        }
        return null;
    }

    @Override
    public InputStream getMessagePayload() throws IOException {
        final String contentType = getContentType();
        if (null != contentType && contentType.startsWith("application/x-www-form-urlencoded")) {
            long contentLength = request.getBody().length();
            ByteArrayOutputStream output = new ByteArrayOutputStream(Long.valueOf(contentLength)
                    .intValue());
            request.getBody().writeTo(output);
            return new ByteArrayInputStream(output.toByteArray());
        }

        throw new UnsupportedOperationException("The content type" + (contentType != null ? " " +
                contentType : "") + " is not supported.");
    }

    @Override
    public String getMethod() {
        return request.getMethod();
    }

    @Override
    public String getRequestUrl() {
        return request.getUrl();
    }

    @Override
    public void setHeader(String key, String value) {
        ArrayList<Header> headers = new ArrayList<Header>();
        headers.addAll(request.getHeaders());
        headers.add(new Header(key, value));
        Request copy = new Request(request.getMethod(), request.getUrl(), headers, request.getBody());
        request = copy;
    }

    @Override
    public void setRequestUrl(String url) {
        Request copy = new Request(request.getMethod(), url, request.getHeaders(), request.getBody());
        request = copy;
    }

    @Override
    public Object unwrap() {
        return request;
    }

}
