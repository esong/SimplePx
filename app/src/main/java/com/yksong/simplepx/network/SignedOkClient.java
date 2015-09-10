package com.yksong.simplepx.network;

import java.io.IOException;

import oauth.signpost.OAuthConsumer;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;

/**
 * Created by esong on 15-09-09.
 */
public class SignedOkClient extends OkClient {
    private final OAuthConsumer mOAuthConsumer;

    public SignedOkClient(String consumerKey, String consumerSecret) {
        super();
        mOAuthConsumer = new RetrofitHttpOAuthConsumer(consumerKey, consumerSecret);
    }

    @Override
    public Response execute(Request request) throws IOException {
        Request requestToSend = request;
        try {
            RetrofitRequestAdapter signedAdapter =
                    (RetrofitRequestAdapter) mOAuthConsumer.sign(request);
            requestToSend = (Request) signedAdapter.unwrap();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return super.execute(requestToSend);
    }
}
