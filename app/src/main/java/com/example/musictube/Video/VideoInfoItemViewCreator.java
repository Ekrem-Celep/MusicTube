package com.example.musictube.Video;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.musictube.Extractor.AbstractVideoInfo;
import com.example.musictube.Extractor.StreamPreviewInfo;
import com.example.musictube.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Ekrem on 2016-05-08.
 */

/* den här klassen ser till att binda ihop informationen från YouTube till layouten som jag har
* skapat*/
public class VideoInfoItemViewCreator {
    private final LayoutInflater inflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).build();

    public VideoInfoItemViewCreator(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public View getViewFromVideoInfoItem(View convertView, ViewGroup parent, StreamPreviewInfo info) {
        ViewHolder holder;

        // Här generera vi alla TextView och ImageView till holder
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.video_item, parent, false);
            holder = new ViewHolder();
            holder.itemThumbnailView = (ImageView) convertView.findViewById(R.id.itemThumbnailView);
            holder.itemVideoTitleView = (TextView) convertView.findViewById(R.id.itemVideoTitleView);
            holder.itemUploaderView = (TextView) convertView.findViewById(R.id.itemUploaderView);
            holder.itemDurationView = (TextView) convertView.findViewById(R.id.itemDurationView);
            holder.itemUploadDateView = (TextView) convertView.findViewById(R.id.itemUploadDateView);
            holder.itemViewCountView = (TextView) convertView.findViewById(R.id.itemViewCountView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Och här fyller vi holder med information

        /*
        if(info.thumbnail == null) {
            holder.itemThumbnailView.setImageResource(R.drawable.dummy_thumbnail);
        } else {
            holder.itemThumbnailView.setImageBitmap(info.thumbnail);
        }
        */
        holder.itemVideoTitleView.setText(info.title);
        if(info.uploader != null && !info.uploader.isEmpty()) {
            holder.itemUploaderView.setText(info.uploader);
        } else {
            holder.itemDurationView.setVisibility(View.INVISIBLE);
        }
        if(info.duration > 0) {
            holder.itemDurationView.setText(getDurationString(info.duration));
        } else {
            if(info.stream_type == AbstractVideoInfo.StreamType.LIVE_STREAM) {
                holder.itemDurationView.setText(R.string.duration_live);
            } else {
                holder.itemDurationView.setVisibility(View.GONE);
            }
        }
        if(info.view_count >= 0) {
            holder.itemViewCountView.setText(shortViewCount(info.view_count));
        } else {
            holder.itemViewCountView.setVisibility(View.GONE);
        }
        if(!info.upload_date.isEmpty()) {
            holder.itemUploadDateView.setText(info.upload_date + " • ");
        }

        holder.itemThumbnailView.setImageResource(R.drawable.dummy_album_cover);
        if(info.thumbnail_url != null && !info.thumbnail_url.isEmpty()) {
            imageLoader.displayImage(info.thumbnail_url, holder.itemThumbnailView, displayImageOptions);
        }

        return convertView;
    }

    private class ViewHolder {
        public ImageView itemThumbnailView;
        public TextView itemVideoTitleView, itemUploaderView, itemDurationView, itemUploadDateView, itemViewCountView;
    }

    private String shortViewCount(Long viewCount){
        if(viewCount >= 1000000000){
            return Long.toString(viewCount/1000000000)+"B views";
        }else if(viewCount>=1000000){
            return Long.toString(viewCount/1000000)+"M views";
        }else if(viewCount>=1000){
            return Long.toString(viewCount/1000)+"K views";
        }else {
            return Long.toString(viewCount)+" views";
        }
    }

    /*  */
    public static String getDurationString(int duration) {
        String output = "";
        int days = duration / (24 * 60 * 60); /* greater than a day */
        duration %= (24 * 60 * 60);
        int hours = duration / (60 * 60); /* greater than an hour */
        duration %= (60 * 60);
        int minutes = duration / 60;
        int seconds = duration % 60;

        //handle days
        if(days > 0) {
            output = Integer.toString(days) + ":";
        }
        // handle hours
        if(hours > 0 || !output.isEmpty()) {
            if(hours > 0) {
                if(hours >= 10 || output.isEmpty()) {
                    output += Integer.toString(minutes);
                } else {
                    output += "0" + Integer.toString(minutes);
                }
            } else {
                output += "00";
            }
            output += ":";
        }
        //handle minutes
        if(minutes > 0 || !output.isEmpty()) {
            if(minutes > 0) {
                if(minutes >= 10 || output.isEmpty()) {
                    output += Integer.toString(minutes);
                } else {
                    output += "0" + Integer.toString(minutes);
                }
            } else {
                output += "00";
            }
            output += ":";
        }

        //handle seconds
        if(output.isEmpty()) {
            output += "0:";
        }

        if(seconds >= 10) {
            output += Integer.toString(seconds);
        } else {
            output += "0" + Integer.toString(seconds);
        }

        return output;
    }
}

