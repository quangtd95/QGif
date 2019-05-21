package com.quangtd.qgifmaker.domain.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.quangtd.qgifmaker.common.base.BaseModel;

public class Photo implements Parcelable, BaseModel {
    private Uri uri;
    private String path;
    private String name;
    private boolean select;
    private int duration;
    private int id;

    protected Photo(Parcel in) {
        id = in.readInt();
        uri = in.readParcelable(Uri.class.getClassLoader());
        path = in.readString();
        name = in.readString();
        select = in.readByte() != 0;
        duration = in.readInt();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Photo() {
    }

    public Photo(int id, String path, String name, int duration) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.duration = duration;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(uri, flags);
        dest.writeString(path);
        dest.writeString(name);
        dest.writeByte((byte) (select ? 1 : 0));
        dest.writeInt(duration);
    }
}
