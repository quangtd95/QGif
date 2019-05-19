package com.quangtd.qgifmaker.domain.repository;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;

import com.quangtd.qgifmaker.domain.model.FolderMedia;
import com.quangtd.qgifmaker.domain.model.Media;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public class VideoRepositoryImpl implements MediaRepository {

    @Override
    public List<FolderMedia> getFolderMedia(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        List<Media> videos = new ArrayList<>();

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
                Media video = new Media(id, path, name, duration);
                videos.add(video);
            }
            c.close();
        }
        return new ArrayList<>(getAlbumVideo(videos));
    }

    private List<FolderMedia> getAlbumVideo(List<Media> videos) {
        List<FolderMedia> albumVideos = new ArrayList<>();

        for (Media video : videos) {
            if (!checkAlbum(albumVideos, video)) {
                List<Media> videos1 = new ArrayList<>();
                videos1.add(video);
                FolderMedia albumVideo = new FolderMedia(videos1, getAlbumName(video.getPath()));
                albumVideo.setPath(getPathAlbum(video.getPath()));
                albumVideos.add(albumVideo);
            }
        }
        return albumVideos;
    }

    private boolean checkAlbum(List<FolderMedia> albumVideos, Media video) {
        boolean check = false;
        for (int i = 0; i < albumVideos.size(); i++) {
            if (getAlbumName(video.getPath()).equals(albumVideos.get(i).getName())) {
                check = true;
                albumVideos.get(i).getListMedia().add(video);
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
