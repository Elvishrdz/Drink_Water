package com.eahm.drinkwaterapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class News implements Parcelable {

    private boolean enabled;
    private String publishedTimestamp;

    private String title;
    private  String content;
    private String contentUrl; //Url general de esta noticia
    private List<NewsMedia> media;

    private int titleTextSize;
    private String colorPrimaryRGB;
    private String colorSecondaryRGB;
    private String colorTextRGB;

    public News() {
    }

    protected News(Parcel in) {
        enabled = in.readInt() == 1;
        publishedTimestamp = in.readString();

        title = in.readString();
        content = in.readString();
        contentUrl = in.readString();
        media = in.createTypedArrayList(NewsMedia.CREATOR);

        titleTextSize = in.readInt();
        colorPrimaryRGB = in.readString();
        colorSecondaryRGB = in.readString();
        colorTextRGB = in.readString();

    }

    //region GET SET

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPublishedTimestamp() {
        return publishedTimestamp;
    }

    public void setPublishedTimestamp(String publishedTimestamp) {
        this.publishedTimestamp = publishedTimestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public List<NewsMedia> getMedia() {
        return media;
    }

    public void setMedia(List<NewsMedia> media) {
        this.media = media;
    }

    public int getTitleTextSize() {
        return titleTextSize;
    }

    public void setTitleTextSize(int titleTextSize) {
        this.titleTextSize = titleTextSize;
    }

    public String getColorPrimaryRGB() {
        return colorPrimaryRGB;
    }

    public void setColorPrimaryRGB(String colorPrimaryRGB) {
        this.colorPrimaryRGB = colorPrimaryRGB;
    }

    public String getColorSecondaryRGB() {
        return colorSecondaryRGB;
    }

    public void setColorSecondaryRGB(String colorSecondaryRGB) {
        this.colorSecondaryRGB = colorSecondaryRGB;
    }

    public String getColorTextRGB() {
        return colorTextRGB;
    }

    public void setColorTextRGB(String colorTextRGB) {
        this.colorTextRGB = colorTextRGB;
    }

    //endregion GET SET

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(enabled ? 1 : 0);
        dest.writeString(publishedTimestamp);

        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(contentUrl);
        dest.writeTypedList(media);

        dest.writeInt(titleTextSize);
        dest.writeString(colorPrimaryRGB);
        dest.writeString(colorSecondaryRGB);
        dest.writeString(colorTextRGB);

    }
}
