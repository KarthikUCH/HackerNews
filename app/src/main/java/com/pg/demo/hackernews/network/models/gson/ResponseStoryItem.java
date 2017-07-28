package com.pg.demo.hackernews.network.models.gson;

/**
 * Created by karthikeyan on 24/7/17.
 */

public class ResponseStoryItem {

    public long id;
    public boolean deleted;
    public String type;
    public String by;
    public long time;
    public String text;
    public boolean dead;
    public long parent;
    public long poll;
    public long[] kids;
    public String url;
    public int score;
    public String title;
    public long[] parts;
    public int descendants = -1;

    // GETTERS
    public long getId() {
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

    public long getTime() {
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
        if (kids == null) {
            return new long[]{};
        }
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

    // SETTERS

    public void setId(long id) {
        this.id = id;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void setParent(long parent) {
        this.parent = parent;
    }

    public void setPoll(long poll) {
        this.poll = poll;
    }

    public void setKids(long[] kids) {
        this.kids = kids;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setParts(long[] parts) {
        this.parts = parts;
    }

    public void setDescendants(int descendants) {
        this.descendants = descendants;
    }
}
