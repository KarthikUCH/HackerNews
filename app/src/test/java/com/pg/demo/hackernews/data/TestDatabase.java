package com.pg.demo.hackernews.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pg.demo.hackernews.mock.MockResponseStoryItem;
import com.pg.demo.hackernews.network.HackersNewsService;
import com.pg.demo.hackernews.network.RestServiceFactory;
import com.pg.demo.hackernews.network.models.gson.ResponseStoryItem;
import com.pg.demo.hackernews.util.FileUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Scheduler;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    StoryDetailsManager mStoryDetailsManager;

    MockResponseStoryItem mockResponseStoryItem;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mContext = Mockito.mock(Context.class);
        mScheduler = Mockito.mock(Scheduler.class);
        mRestServiceFactory = Mockito.mock(RestServiceFactory.class);

        File Database = FileUtil.getFileFromPath(this, DB_NAME);
        mDbPath = Database.getPath();
        mDbHelper = SQLiteDatabase.openDatabase(mDbPath, null, SQLiteDatabase.OPEN_READWRITE);

        mTopStoryManager = new TopStoryManager(mContext, mDbHelper, mScheduler, mRestServiceFactory);
        mStoryDetailsManager = new StoryDetailsManager(mContext, mDbHelper, mScheduler, mTopStoryManager);
    }

    @After
    public void tearDown() throws Exception {
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

    @Test
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

    @Test
    public void testRetrieveTopStories() throws Exception {
        String fileNameTopStory = "top_item_200_ok_response.json";
        String topStoryResponse = FileUtil.getFileContentFromPath(this, fileNameTopStory);
        List<Long> lst;
        Type listType = new TypeToken<List<Long>>() {
        }.getType();
        lst = new Gson().fromJson(topStoryResponse, listType);

        String fileNameItemDetails = "item_detail_success_insert.json";
        String itemDetailsResponse = FileUtil.getFileContentFromPath(this, fileNameItemDetails);
        ResponseStoryItem itemResponse;
        Type itemResponseType = new TypeToken<ResponseStoryItem>() {
        }.getType();
        itemResponse = new Gson().fromJson(itemDetailsResponse, itemResponseType);

        HackersNewsService service = mock(HackersNewsService.class);
        when(mRestServiceFactory.create(HackersNewsService.class)).thenReturn(service);

        Call<List<Long>> callList = mock(Call.class);
        when(service.getTopStories()).thenReturn(callList);
        when(callList.execute()).thenReturn(Response.success(lst));


        Call<ResponseStoryItem> callDetail = mock(Call.class);
        when(service.getStoryDetail(anyString())).thenReturn(callDetail);
        when(callDetail.execute()).thenReturn(Response.success(itemResponse));


        Observable<ResponseStoryItem> topStoryObservable = mTopStoryManager.getTopStoriesObservable();
        TestSubscriber<ResponseStoryItem> topStorySubscriber = new TestSubscriber<>();
        topStoryObservable.subscribe(topStorySubscriber);

        topStorySubscriber.assertCompleted();

    }

    @Test
    public void testRetrieveStoryComments() throws Exception {

        String fileNameItemDetails = "item_detail_success_insert.json";
        String itemDetailsResponse = FileUtil.getFileContentFromPath(this, fileNameItemDetails);
        ResponseStoryItem itemResponse;
        Type itemResponseType = new TypeToken<ResponseStoryItem>() {
        }.getType();
        itemResponse = new Gson().fromJson(itemDetailsResponse, itemResponseType);

        HackersNewsService service = mock(HackersNewsService.class);
        when(mRestServiceFactory.create(HackersNewsService.class)).thenReturn(service);


        Call<ResponseStoryItem> callDetail = mock(Call.class);
        when(service.getStoryDetail(anyString())).thenReturn(callDetail);
        when(callDetail.execute()).thenReturn(Response.success(itemResponse));

        ArrayList<Long> ids = new ArrayList<>();
        ids.add(14871127L);
        Observable<ArrayList<Long>> topStoryObservable = mStoryDetailsManager.retrieveStoryCommentsObservable(ids);
        TestSubscriber<ArrayList<Long>> topStorySubscriber = new TestSubscriber<>();
        topStoryObservable.subscribe(topStorySubscriber);

        topStorySubscriber.assertCompleted();


    }
}
