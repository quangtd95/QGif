package com.quangtd.qgifmaker.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.quangtd.qgifmaker.common.base.BaseModel;

import java.util.List;

public class FolderPhoto implements BaseModel, Parcelable {
    private List<Photo> photos;
    private String name;
    private String path;

    public FolderPhoto(List<Photo> photos, String name) {
        this.photos = photos;
        this.name = name;
    }

    protected FolderPhoto(Parcel in) {
        photos = in.createTypedArrayList(Photo.CREATOR);
        name = in.readString();
        path = in.readString();
    }

    public static final Creator<FolderPhoto> CREATOR = new Creator<FolderPhoto>() {
        @Override
        public FolderPhoto createFromParcel(Parcel in) {
            return new FolderPhoto(in);
        }

        @Override
        public FolderPhoto[] newArray(int size) {
            return new FolderPhoto[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public String getName() {
        return name;
    }

    public String getFirstImage() {
        if (photos != null && !photos.isEmpty()) {
            return photos.get(0).getPath();
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(photos);
        dest.writeString(name);
        dest.writeString(path);
    }
}
