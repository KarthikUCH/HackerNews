package com.pg.demo.hackernews.network.models;

import java.util.List;

/**
 * Created by karthikeyan on 23/7/17.
 */

public class StoryItem {

    public Long id;
    private boolean deleted;
    private String type;
    public String by;
    public String time;
    private String text;
    private boolean dead;
    private long parent;
    private long poll;
    private long[] kids;
    private String url;
    private int score;
    private String title;
    private long[] parts;
    private int descendants = -1;


    public Long getId() {
        return id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public String getType() {
        return type;
    }

    public String getBy() {
        return by;
    }

    public String getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    public boolean isDead() {
        return dead;
    }

    public long getParent() {
        return parent;
    }

    public long getPoll() {
        return poll;
    }

    public long[] getKids() {
        return kids;
    }

    public String getUrl() {
        return url;
    }

    public int getScore() {
        return score;
    }

    public String getTitle() {
        return title;
    }

    public long[] getParts() {
        return parts;
    }

    public int getDescendants() {
        return descendants;
    }
}
