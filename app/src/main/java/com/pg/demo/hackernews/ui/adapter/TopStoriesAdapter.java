package com.pg.demo.hackernews.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pg.demo.hackernews.R;
import com.pg.demo.hackernews.network.models.gson.ResponseStoryItem;

import java.util.ArrayList;

/**
 * Created by karthikeyan on 25/7/17.
 */

public class TopStoriesAdapter extends RecyclerView.Adapter<TopStoriesAdapter.TopStoryViewHolder> {

    private ArrayList<ResponseStoryItem> mTopStoryList;
    private final ClickListner mClickListner;
    private final Context mContext;

    public interface ClickListner {
        void onCLick(long storyId);
    }

    public TopStoriesAdapter(Context context, ArrayList<ResponseStoryItem> topStoryList, ClickListner clickListner) {

        mContext = context;
        mTopStoryList = topStoryList;
        mClickListner = clickListner;
    }

    @Override
    public TopStoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_top_story, parent, false);
        return new TopStoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TopStoriesAdapter.TopStoryViewHolder holder, int position) {

        ResponseStoryItem story = mTopStoryList.get(position);

        holder.tvTitle.setText(story.getTitle());
        holder.tvUser.setText(story.getBy());
        CharSequence time = DateUtils.formatDateTime(
                mContext,
                story.getTime(),
                DateUtils.FORMAT_ABBREV_ALL);
        holder.tvTime.setText(time);
        holder.tvComments.setText(story.getDescendants() + " Comments");
        holder.tvScore.setText(String.valueOf(story.getScore()));

        long storyId = story.getId();
        holder.itemView.setOnClickListener(v -> mClickListner.onCLick(storyId));


    }

    @Override
    public int getItemCount() {
        return mTopStoryList.size();
    }

    public void swapItems(ArrayList<ResponseStoryItem> topStoryList) {
        mTopStoryList = topStoryList;
        notifyDataSetChanged();
    }

    public void addItem(ResponseStoryItem storyItem) {
        mTopStoryList.add(storyItem);
        notifyItemInserted(mTopStoryList.size() - 1);
    }

    class TopStoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvUser;
        TextView tvTime;
        TextView tvComments;
        TextView tvScore;

        public TopStoryViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvUser = (TextView) itemView.findViewById(R.id.tv_post_by);
            tvTime = (TextView) itemView.findViewById(R.id.tv_post_time);
            tvComments = (TextView) itemView.findViewById(R.id.tv_comments_count);
            tvScore = (TextView) itemView.findViewById(R.id.tv_score);
        }
    }
}
