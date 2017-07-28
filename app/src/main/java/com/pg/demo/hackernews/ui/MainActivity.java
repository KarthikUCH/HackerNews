package com.pg.demo.hackernews.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.pg.demo.hackernews.R;
import com.pg.demo.hackernews.application.ApplicationComponent;
import com.pg.demo.hackernews.data.TopStoryManager;
import com.pg.demo.hackernews.network.models.gson.ResponseStoryItem;
import com.pg.demo.hackernews.ui.adapter.TopStoriesAdapter;

import java.util.ArrayList;

public class MainActivity extends InjectableActivity implements TopStoryManager.Observer, TopStoriesAdapter.ClickListner {

    SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private TopStoriesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_top_story);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_layout);
        loadTopStories();

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mTopStoryManager.onRefresh();
        });
    }

    @Override
    void injectComponent(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTopStoryManager.attach(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTopStoryManager.detach();
    }

    private void loadTopStories() {
        mAdapter = new TopStoriesAdapter(getApplicationContext(), new ArrayList<>(), this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onStoriesLoaded(ArrayList<ResponseStoryItem> topStories) {
        mAdapter.swapItems(topStories);
    }

    @Override
    public void addStory(ResponseStoryItem storyItem) {
        mAdapter.addItem(storyItem);
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    @Override
    public void onCLick(long storyId) {
        Intent intent = new Intent(this, StoryDetailActivity.class);
        intent.putExtra(StoryDetailActivity.EXTRA_INT_STORY_ID, storyId);
        startActivity(intent);
    }
}
