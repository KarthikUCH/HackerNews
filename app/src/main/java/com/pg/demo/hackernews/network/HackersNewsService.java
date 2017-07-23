package com.pg.demo.hackernews.network;

import com.pg.demo.hackernews.network.models.StoryItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by karthikeyan on 23/7/17.
 */

public interface HackersNewsService {

    @GET("topstories.json")
    Call<List<Long>> getTopStories();

    @GET("item/{id}")
    Call<StoryItem> getStoryDetail(@Path("id") String id);
}
