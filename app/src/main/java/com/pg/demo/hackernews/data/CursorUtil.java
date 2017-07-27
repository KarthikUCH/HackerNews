package com.pg.demo.hackernews.data;

import android.database.Cursor;

import com.pg.demo.hackernews.data.DbConstants.*;
import com.pg.demo.hackernews.network.models.StoryDetail;
import com.pg.demo.hackernews.network.models.gson.ResponseStoryItem;

/**
 * Created by karthikeyan on 24/7/17.
 */

public class CursorUtil {

    public static ResponseStoryItem getStory(Cursor cursor) {
        ResponseStoryItem item = new ResponseStoryItem();
        item.setId(cursor.getLong(cursor.getColumnIndex(ItemDetail.COLUMN_ITEM_ID)));
        item.setParent(cursor.getLong(cursor.getColumnIndex(ItemDetail.COLUMN_ITEM_PARENT)));
        item.setType(cursor.getString(cursor.getColumnIndex(ItemDetail.COLUMN_ITEM_TYPE)));
        item.setBy(cursor.getString(cursor.getColumnIndex(ItemDetail.COLUMN_ITEM_POST_BY)));
        item.setTime(cursor.getLong(cursor.getColumnIndex(ItemDetail.COLUMN_ITEM_POSTED_TIME)));
        item.setText(cursor.getString(cursor.getColumnIndex(ItemDetail.COLUMN_ITEM_TEXT)));
        item.setPoll(cursor.getLong(cursor.getColumnIndex(ItemDetail.COLUMN_ITEM_POLL)));
        item.setUrl(cursor.getString(cursor.getColumnIndex(ItemDetail.COLUMN_ITEM_URL)));
        item.setScore(cursor.getInt(cursor.getColumnIndex(ItemDetail.COLUMN_ITEM_SCORE)));
        item.setTitle(cursor.getString(cursor.getColumnIndex(ItemDetail.COLUMN_ITEM_TITLE)));
        item.setDescendants(cursor.getInt(cursor.getColumnIndex(ItemDetail.COLUMN_ITEM_COMMENT_COUNT)));

        return item;
    }

    public static StoryDetail getStoryDetails(Cursor cursor) {
        StoryDetail storyDetail = new StoryDetail();
        storyDetail.setStoryItem(getStory(cursor));
        storyDetail.setLevel(cursor.getInt(cursor.getColumnIndex("level")));

        return storyDetail;
    }
}
