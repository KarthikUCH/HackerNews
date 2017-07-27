package com.pg.demo.hackernews.network;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.pg.demo.hackernews.data.TopStoryManager;
import com.pg.demo.hackernews.network.models.gson.ResponseStoryItem;
import com.pg.demo.hackernews.util.FileUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;


import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

/**
 * Created by karthikeyan on 27/7/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class TestTopItems {

    private MockWebServer mMockWebServer;

    @Mock
    Context mContext;
    @Mock
    SQLiteDatabase mDbHelper;
    @Mock
    Scheduler mScheduler;
    @Mock
    RestServiceFactory mRestServiceFactory;


    TopStoryManager mTopStoryManager;

    @Before
    public void setUp() throws Exception {
        mMockWebServer = new MockWebServer();
        mMockWebServer.start();

        mTopStoryManager = new TopStoryManager(mContext, mDbHelper, mScheduler, mRestServiceFactory);
    }

    @Test
    public void testTopItems_Success() throws Exception {
        String fileName = "top_item_200_ok_response.json";
        mMockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(FileUtil.getFileFromPath(this, fileName)));

        when(mRestServiceFactory.create(HackersNewsService.class)).thenReturn(getHackersNewsApiService());

        List<Long> topItems = mTopStoryManager.retrieveTopStoryIds();

        assertEquals(checkSize(topItems), true);
    }

    @Test
    public void testItemDetail_Success() throws Exception {
        String fileName = "item_detail_200_0k.json";
        long itemId = 14865112L;
        mMockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(FileUtil.getFileFromPath(this, fileName)));

        when(mRestServiceFactory.create(HackersNewsService.class)).thenReturn(getHackersNewsApiService());
        ResponseStoryItem responseStoryItem = mTopStoryManager.retrieveStoryDetail(itemId);
        assertEquals(responseStoryItem.getId(), itemId);
    }

    boolean checkSize(List<Long> topItems) {
        return topItems.size() > 0;
    }

    public HackersNewsService getHackersNewsApiService() {
        HackersNewsService service = new Retrofit.Builder()
                .baseUrl(mMockWebServer.url("").toString())
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(HackersNewsService.class);
        return service;
    }

}
