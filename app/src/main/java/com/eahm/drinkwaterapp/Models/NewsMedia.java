package com.eahm.drinkwaterapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsMedia implements Parcelable{

    private boolean enabled;
    private boolean isVideo; // true:video false:image

    private String coverImageUrl; // imagen ó thumbnail (en caso de video)
    private String coverImageScaleType; // opcional. tipo de escala de la imagen

    private String mediaUrl; // este campo queda vacio si es una imagen
    private String mediaOnClickUrl; // url para ir a este contenido

    public NewsMedia() {
    }

    public NewsMedia(boolean enabled, boolean isVideo, String coverImageUrl, String coverImageScaleType, String mediaUrl, String mediaOnClickUrl) {
        this.enabled = enabled;
        this.isVideo = isVideo;
        this.coverImageUrl = coverImageUrl;
        this.coverImageScaleType = coverImageScaleType;
        this.mediaUrl = mediaUrl;
        this.mediaOnClickUrl = mediaOnClickUrl;
    }

    protected NewsMedia(Parcel in) {
        enabled = in.readInt() == 1;
        isVideo = in.readInt() == 1;
        coverImageUrl = in.readString();
        coverImageScaleType = in.readString();
        mediaUrl = in.readString();
        mediaOnClickUrl = in.readString();
    }

    //region GET SET

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getCoverImageScaleType() {
        return coverImageScaleType;
    }

    public void setCoverImageScaleType(String coverImageScaleType) {
        this.coverImageScaleType = coverImageScaleType;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaOnClickUrl() {
        return mediaOnClickUrl;
    }

    public void setMediaOnClickUrl(String mediaOnClickUrl) {
        this.mediaOnClickUrl = mediaOnClickUrl;
    }

    //endregion GET SET

    public static final Creator<NewsMedia> CREATOR = new Creator<NewsMedia>() {
        @Override
        public NewsMedia createFromParcel(Parcel in) {
            return new NewsMedia(in);
        }

        @Override
        public NewsMedia[] newArray(int size) {
            return new NewsMedia[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(enabled ? 1 : 0);
        dest.writeInt(isVideo ? 1 : 0);
        dest.writeString(coverImageUrl);
        dest.writeString(coverImageScaleType);
        dest.writeString(mediaUrl);
        dest.writeString(mediaOnClickUrl);
    }
}
