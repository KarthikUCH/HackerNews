package com.pg.demo.hackernews.mock;

import com.pg.demo.hackernews.network.models.gson.ResponseStoryItem;

/**
 * Created by karthikeyan on 28/7/17.
 */

public class MockResponseStoryItem {

    private long id = 14871127;
    private boolean deleted = false;
    private String type = "story";
    private String by = "iloveluce";
    private long time = 1501204048;
    private String text = "";
    private boolean dead;
    private long parent = 0;
    private long poll = 120;
    private long[] kids;
    private String url = "http://www.hiringlab.org/2017/07/25/next-silicon-valley/";
    private int score = 100;
    private String title = "SpaceX Is Now One of the World’s Most Valuable Privately Held Companies";
    private long[] parts;
    private int descendants = 20;

    ResponseStoryItem responseStoryItem = new ResponseStoryItem();

    public MockResponseStoryItem() {
        responseStoryItem = new ResponseStoryItem();
        responseStoryItem.setId(id);
        responseStoryItem.setType(type);
        responseStoryItem.setBy(by);
        responseStoryItem.setTime(time);
        responseStoryItem.setText(text);
        responseStoryItem.setParent(parent);
        responseStoryItem.setPoll(poll);
        responseStoryItem.setUrl(url);
        responseStoryItem.setScore(score);
        responseStoryItem.setTitle(title);
        responseStoryItem.setDescendants(descendants);
    }

    public ResponseStoryItem getResponseStoryItem() {
        return responseStoryItem;
    }
}


