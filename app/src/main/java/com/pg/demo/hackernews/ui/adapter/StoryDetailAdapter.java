package com.pg.demo.hackernews.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pg.demo.hackernews.R;
import com.pg.demo.hackernews.Util.AppUtil;
import com.pg.demo.hackernews.network.models.StoryDetail;
import com.pg.demo.hackernews.network.models.gson.ResponseStoryItem;

import java.util.ArrayList;

/**
 * Created by karthikeyan on 26/7/17.
 */

public class StoryDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_STORY_ITEM = 1;
    private ArrayList<StoryDetail> mStoryDetails;
    private final Context mContext;

    public StoryDetailAdapter(Context context, ArrayList<StoryDetail> storyDetails) {
        mContext = context;
        mStoryDetails = storyDetails;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case VIEW_TYPE_STORY_ITEM:
                view = inflater.inflate(R.layout.item_top_story, parent, false);
                return new StoryItemViewHolder(view);
            default:
                view = inflater.inflate(R.layout.item_story_comments, parent, false);
                return new StoryCommentViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mStoryDetails.get(position).getLevel();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (mStoryDetails.get(position).getLevel()) {
            case VIEW_TYPE_STORY_ITEM:
                StoryItemViewHolder storyItemViewHolder = (StoryItemViewHolder) holder;
                onBindStoryItemViewHolder(storyItemViewHolder, position);
                break;
            default:
                StoryCommentViewHolder storyCommentViewHolder = (StoryCommentViewHolder) holder;
                onBindStoryCommentViewHolder(storyCommentViewHolder, position);
                break;
        }
    }

    void onBindStoryItemViewHolder(StoryItemViewHolder holder, int position) {

        StoryDetail story = mStoryDetails.get(position);
        ResponseStoryItem storyItem = story.getStoryItem();

        holder.tvTitle.setText(storyItem.getTitle());
        holder.tvUser.setText(storyItem.getBy());
        SpannableString timeSpannable = new SpannableString(
                AppUtil.getAbbreviatedTimeSpan(storyItem.getTime() * 1000));
        holder.tvTime.setText(timeSpannable);
        holder.tvComments.setText(storyItem.getDescendants() + " Comments");
        holder.tvScore.setText(String.valueOf(storyItem.getScore()));

    }

    void onBindStoryCommentViewHolder(StoryCommentViewHolder holder, int position) {
        StoryDetail story = mStoryDetails.get(position);
        ResponseStoryItem storyItem = story.getStoryItem();

        String text = storyItem.getText();
        if (text != null) {
            holder.tvText.setText(fromHtml(text));
        }
        holder.tvText.setVisibility(text != null ? View.VISIBLE : View.INVISIBLE);
        holder.tvUser.setText(storyItem.getBy());
        SpannableString timeSpannable = new SpannableString(
                AppUtil.getAbbreviatedTimeSpan(storyItem.getTime() * 1000));
        holder.tvTime.setText(timeSpannable);
    }

    @Override
    public int getItemCount() {
        return mStoryDetails.size();
    }

    public void swapItems(ArrayList<StoryDetail> storyDetails) {
        mStoryDetails = storyDetails;
        notifyDataSetChanged();
    }

    class StoryCommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvText;
        TextView tvUser;
        TextView tvTime;

        public StoryCommentViewHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tv_text);
            tvUser = (TextView) itemView.findViewById(R.id.tv_post_by);
            tvTime = (TextView) itemView.findViewById(R.id.tv_post_time);
        }
    }

    class StoryItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvUser;
        TextView tvTime;
        TextView tvComments;
        TextView tvScore;

        public StoryItemViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvUser = (TextView) itemView.findViewById(R.id.tv_post_by);
            tvTime = (TextView) itemView.findViewById(R.id.tv_post_time);
            tvComments = (TextView) itemView.findViewById(R.id.tv_comments_count);
            tvScore = (TextView) itemView.findViewById(R.id.tv_score);
        }
    }


    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}
