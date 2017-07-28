package com.pg.demo.hackernews.ui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pg.demo.hackernews.R;
import com.pg.demo.hackernews.application.ApplicationComponent;
import com.pg.demo.hackernews.data.StoryDetailsManager;
import com.pg.demo.hackernews.network.models.StoryDetail;
import com.pg.demo.hackernews.ui.adapter.StoryDetailAdapter;

import java.util.ArrayList;

public class StoryDetailActivity extends InjectableActivity implements StoryDetailsManager.Observer {

    public static final String EXTRA_INT_STORY_ID = "EXTRA_INTEGER_STORY_ID";
    private long storyId;

    private RecyclerView mRecyclerView;
    private StoryDetailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_story_detail);
        loadDetails();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mStoryDetailsManager.attach(this, storyId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mStoryDetailsManager.detach();
    }

    private void loadDetails() {
        storyId = getIntent().getLongExtra(EXTRA_INT_STORY_ID, -1);
        mAdapter = new StoryDetailAdapter(getApplicationContext(), new ArrayList<>());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    void injectComponent(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    public void onStoriesLoaded(ArrayList<StoryDetail> storyDetails) {
        mAdapter.swapItems(storyDetails);
    }
}
