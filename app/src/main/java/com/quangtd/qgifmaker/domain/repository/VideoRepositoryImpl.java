package com.quangtd.qgifmaker.domain.repository;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;

import com.quangtd.qgifmaker.domain.model.FolderPhoto;
import com.quangtd.qgifmaker.domain.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public class VideoRepositoryImpl implements PhotoRepository {

    @Override
    public List<FolderPhoto> getFolderMedia(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        List<Photo> videos = new ArrayList<>();

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA, MediaStore.Video.VideoColumns.DURATION, MediaStore.Video.VideoColumns._ID};
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        if (c != null) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                String path = c.getString(0);
                String name = getVideoName(path);
                int duration = c.getInt(1);
                int id = c.getInt(2);
                Photo video = new Photo(id, path, name, duration);
                videos.add(video);
            }
            c.close();
        }
        return new ArrayList<>(getAlbumVideo(videos));
    }

    private List<FolderPhoto> getAlbumVideo(List<Photo> videos) {
        List<FolderPhoto> albumVideos = new ArrayList<>();

        for (Photo video : videos) {
            if (!checkAlbum(albumVideos, video)) {
                List<Photo> videos1 = new ArrayList<>();
                videos1.add(video);
                FolderPhoto albumVideo = new FolderPhoto(videos1, getAlbumName(video.getPath()));
                albumVideo.setPath(getPathAlbum(video.getPath()));
                albumVideos.add(albumVideo);
            }
        }
        return albumVideos;
    }

    private boolean checkAlbum(List<FolderPhoto> albumVideos, Photo video) {
        boolean check = false;
        for (int i = 0; i < albumVideos.size(); i++) {
            if (getAlbumName(video.getPath()).equals(albumVideos.get(i).getName())) {
                check = true;
                albumVideos.get(i).getPhotos().add(video);
                break;
            }
            check = false;
        }
        return check;
    }

    private String getAlbumName(String path) {
        String[] names = path.split("/");
        return names[names.length - 2];
    }

    private String getPathAlbum(String path) {
        String[] names = path.split("/");
        return path.substring(0, path.length() - names[names.length - 1].length());
    }

    private String getVideoName(String path) {
        String[] names = path.split("/");
        return names[names.length - 1];
    }

}
