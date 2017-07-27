package com.pg.demo.hackernews.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by karthikeyan on 27/7/17.
 */

public interface RestServiceFactory {

    String API_BASE_URL = "https://hacker-news.firebaseio.com/v0/";

    <T> T create(Class<T> clazz);

    class Impl implements RestServiceFactory {

        @Override
        public <T> T create(Class<T> clazz) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder
                    .client(httpClient.build())
                    .build();
            return retrofit.create(clazz);
        }
    }
}
