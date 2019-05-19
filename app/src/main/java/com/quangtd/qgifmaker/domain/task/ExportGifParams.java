package com.quangtd.qgifmaker.domain.task;

import com.quangtd.qgifmaker.domain.model.Media;
import com.quangtd.qgifmaker.domain.model.MediaType;

import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public class ExportGifParams {
    private MediaType mediaType;
    private int delay;
    private int width;
    private int height;
    private List<Media> photos;
    private Media video;
    private int startTime;
    private int endTime;
    private String resultPath;

    public ExportGifParams(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getResultPath() {
        return resultPath;
    }

    public void setResultPath(String resultPath) {
        this.resultPath = resultPath;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Media> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Media> photos) {
        this.photos = photos;
    }

    public Media getVideo() {
        return video;
    }

    public void setVideo(Media video) {
        this.video = video;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }
}
