package com.example.hoang_movie.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.hoang_movie.network.Api;
import com.example.hoang_movie.utils.Constants;
import com.squareup.moshi.Json;

public class Video implements Parcelable {
    public static final String SITE_YOUTUBE = "YouTube";
    private String id;
    private String name;
    private String site;
    @Json(name = "key")
    private String videoId;
    private int size;
    private String type;

    public Video() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    protected Video(Parcel in) {
        id = in.readString();
        name = in.readString();
        site = in.readString();
        videoId = in.readString();
        size = in.readInt();
        type = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
    public static String getUrl(Video video){
        if(SITE_YOUTUBE.equalsIgnoreCase(video.getSite())){
            return String.format(Api.YOUTUBE_THUMBNAIL_URL, video.getVideoId());
        }else {
            return Constants.EMPTY;
        }
    }
    public static String getThumbnailUrl(Video video) {
        if (SITE_YOUTUBE.equalsIgnoreCase(video.getSite())) {
            return String.format(Api.YOUTUBE_THUMBNAIL_URL, video.getVideoId());
        } else {
            return Constants.EMPTY;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(site);
        parcel.writeString(videoId);
        parcel.writeInt(size);
        parcel.writeString(type);
    }
}
