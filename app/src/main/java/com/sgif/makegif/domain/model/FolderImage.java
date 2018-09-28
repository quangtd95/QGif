package com.sgif.makegif.domain.model;

import com.sgif.makegif.common.base.BaseModel;

import java.util.List;

public class FolderImage implements BaseModel {
    private List<Photo> photos;
    private String name;
    private String path;

    public FolderImage(List<Photo> photos, String name) {
        this.photos = photos;
        this.name = name;
    }

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
}
