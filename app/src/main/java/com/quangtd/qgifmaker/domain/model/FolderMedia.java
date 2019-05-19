package com.quangtd.qgifmaker.domain.model;

import com.quangtd.qgifmaker.common.base.BaseModel;

import java.util.List;

public class FolderMedia implements BaseModel {
    private List<Media> photos;
    private String name;
    private String path;

    public FolderMedia(List<Media> photos, String name) {
        this.photos = photos;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Media> getListMedia() {
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
