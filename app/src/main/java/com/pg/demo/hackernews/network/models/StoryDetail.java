package com.pg.demo.hackernews.network.models;

import com.pg.demo.hackernews.network.models.gson.ResponseStoryItem;

/**
 * Created by karthikeyan on 26/7/17.
 */

public class StoryDetail {
    int level;

    ResponseStoryItem storyItem;

    public void setLevel(int level) {
        this.level = level;
    }

    public void setStoryItem(ResponseStoryItem storyItem) {
        this.storyItem = storyItem;
    }

    public int getLevel() {
        return level;
    }

    public ResponseStoryItem getStoryItem() {
        return storyItem;
    }
}
