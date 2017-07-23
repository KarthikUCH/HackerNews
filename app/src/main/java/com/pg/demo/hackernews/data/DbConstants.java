package com.pg.demo.hackernews.data;

import android.provider.BaseColumns;

/**
 * Created by karthikeyan on 23/7/17.
 */

public class DbConstants {


    public static final String DB_NAME = "hackersnews.db";
    public static final int DB_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String LONG_TYPE = " INTEGER";
    private static final String AUTO_INCREMENT_TYPE = " AUTOINCREMENT";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String COMMA_SEP = ",";

    interface Tables {
        String TOP_STORIES = "top_stories";
        String ITEM_DETAIL = "item_detail";
    }

    interface TopStories extends BaseColumns {
        String COLUMN_TOP_STORY_ID = "top_story_id";
    }

    interface ItemDetail extends BaseColumns {
        String COLUMN_ITEM_ID = "item_id";
        String COLUMN_ITEM_PARENT = "item_parent";
        String COLUMN_ITEM_TYPE = "item_type";
        String COLUMN_ITEM_POST_BY = "item_post_by";
        String COLUMN_ITEM_POSTED_TIME = "item_posted_time";
        String COLUMN_ITEM_TEXT = "item_text";
        String COLUMN_ITEM_POLL = "item_poll";
        String COLUMN_ITEM_URL = "item_url";
        String COLUMN_ITEM_SCORE = "item_score";
        String COLUMN_ITEM_TITLE = "item_title";
    }

    // CREATE TABLE SQL QUERY

    public static final String SQL_CREATE_TOP_STORIES_TABLE =
            "CREATE TABLE " + Tables.TOP_STORIES + " (" +
                    TopStories._ID + INTEGER_TYPE + PRIMARY_KEY + AUTO_INCREMENT_TYPE + COMMA_SEP +
                    TopStories.COLUMN_TOP_STORY_ID + LONG_TYPE + COMMA_SEP +
                    " UNIQUE (" + TopStories.COLUMN_TOP_STORY_ID + ") ON CONFLICT IGNORE)";

    public static final String SQL_CREATE_ITEM_DETAILS_TABLE =
            "CREATE TABLE " + Tables.ITEM_DETAIL + " (" +
                    ItemDetail._ID + INTEGER_TYPE + PRIMARY_KEY + AUTO_INCREMENT_TYPE + COMMA_SEP +
                    ItemDetail.COLUMN_ITEM_ID + LONG_TYPE + COMMA_SEP +
                    ItemDetail.COLUMN_ITEM_PARENT + LONG_TYPE + COMMA_SEP +
                    ItemDetail.COLUMN_ITEM_TITLE + TEXT_TYPE + COMMA_SEP +
                    ItemDetail.COLUMN_ITEM_TYPE + TEXT_TYPE + COMMA_SEP +
                    ItemDetail.COLUMN_ITEM_POST_BY + TEXT_TYPE + COMMA_SEP +
                    ItemDetail.COLUMN_ITEM_POSTED_TIME + LONG_TYPE + COMMA_SEP +
                    ItemDetail.COLUMN_ITEM_TEXT + TEXT_TYPE + COMMA_SEP +
                    ItemDetail.COLUMN_ITEM_POLL + INTEGER_TYPE + COMMA_SEP +
                    ItemDetail.COLUMN_ITEM_URL + TEXT_TYPE + COMMA_SEP +
                    ItemDetail.COLUMN_ITEM_SCORE + TEXT_TYPE + COMMA_SEP +
                    " UNIQUE (" + ItemDetail.COLUMN_ITEM_ID + ") ON CONFLICT IGNORE)";


    // DROP TABLE SQL QUERY

    public static final String SQL_DROP_TOP_STORIES = "DROP TABLE IF EXISTS" + Tables.TOP_STORIES;
    public static final String SQL_DROP_ITEM_DETAILS = "DROP TABLE IF EXISTS" + Tables.TOP_STORIES;
}
