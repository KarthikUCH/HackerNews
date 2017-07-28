package com.pg.demo.hackernews.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pg.demo.hackernews.mock.MockResponseStoryItem;
import com.pg.demo.hackernews.network.RestServiceFactory;
import com.pg.demo.hackernews.network.models.gson.ResponseStoryItem;
import com.pg.demo.hackernews.util.FileUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.util.ArrayList;

import rx.Scheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by karthikeyan on 28/7/17.
 */

@RunWith(RobolectricTestRunner.class)
public class TestDatabase {

    private static final String DB_NAME = "hackersnews.db";
    private String mDbPath;
    SQLiteDatabase mDbHelper;

    Context mContext;
    Scheduler mScheduler;
    RestServiceFactory mRestServiceFactory;

    TopStoryManager mTopStoryManager;

    MockResponseStoryItem mockResponseStoryItem;


    @Before
    public void setUp() throws Exception {

        File Database = FileUtil.getFileFromPath(this, DB_NAME);
        mDbPath = Database.getPath();
        mDbHelper = SQLiteDatabase.openDatabase(mDbPath, null, SQLiteDatabase.OPEN_READWRITE);

        mTopStoryManager = new TopStoryManager(mContext, mDbHelper, mScheduler, mRestServiceFactory);
    }

    @After
    public void tearDown() {
        mDbHelper.close();
    }

    @Test
    public void testTopStoryList_Success() {
        ArrayList<ResponseStoryItem> storyList = mTopStoryManager.getTopStories();
        assertNotNull(storyList);
    }

    @Test
    public void testInsertTopStoryId_Success() {
        long rowId = mTopStoryManager.insertTopStoryId(123456);
        assertTrue(rowId > 0);
    }

    public void testInsertStoryDetail_Success() {
        mockResponseStoryItem = new MockResponseStoryItem();
        long rowId = mTopStoryManager.insertItemDetails(mockResponseStoryItem.getResponseStoryItem());
        assertTrue(rowId > 0);
    }

    @Test
    public void testIfStoryDetailExists_Success() {
        boolean isExists = mTopStoryManager.checkStoryExists(14871127);
        assertTrue(isExists);
    }

    @Test
    public void testIfStoryDetailExists_Failure() {
        boolean isExists = mTopStoryManager.checkStoryExists(123456);
        assertFalse(isExists);
    }
}
