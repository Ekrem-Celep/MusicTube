package com.example.musictube.Video;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.musictube.Extractor.StreamPreviewInfo;
import com.example.musictube.Fragments.VideoItemListFragment;
import com.example.musictube.R;
import com.example.musictube.Video.VideoInfoItemViewCreator;

import java.util.List;
import java.util.Vector;

/**
 * Created by Ekrem on 2016-05-08.
 */
public class VideoListAdapter extends BaseAdapter {
    private final Context context;
    private final VideoInfoItemViewCreator viewCreator;
    private Vector<StreamPreviewInfo> videoList = new Vector<>();
    private final ListView listView;

    public VideoListAdapter(Context context, VideoItemListFragment videoListFragment) {
        viewCreator = new VideoInfoItemViewCreator(LayoutInflater.from(context));
        this.listView = videoListFragment.getListView();
        this.listView.setDivider(null);
        this.listView.setDividerHeight(0);
        this.context = context;
    }

    public void addVideoList(List<StreamPreviewInfo> videos) {
        videoList.addAll(videos);
        notifyDataSetChanged();
    }

    public void clearVideoList() {
        videoList = new Vector<>();
        notifyDataSetChanged();
    }

    public Vector<StreamPreviewInfo> getVideoList() {
        return videoList;
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int position) {
        return videoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = viewCreator.getViewFromVideoInfoItem(convertView, parent, videoList.get(position));

        if(listView.isItemChecked(position)) {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            convertView.setBackgroundColor(0);
        }

        return convertView;
    }
}
