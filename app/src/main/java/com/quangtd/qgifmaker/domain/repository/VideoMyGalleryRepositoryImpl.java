package com.quangtd.qgifmaker.domain.repository;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.quangtd.qgifmaker.common.Constants;
import com.quangtd.qgifmaker.domain.model.Photo;
import com.quangtd.qgifmaker.util.FileUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by QuangTD
 * on 3/14/2018.
 */

public class VideoMyGalleryRepositoryImpl implements VideoMyGalleryRepository {
    private Context context;

    public VideoMyGalleryRepositoryImpl(Context context) {
        this.context = context;
    }

    private List<Photo> dumpVideos() {
        List<Photo> videos = new ArrayList<>();
        String folder = Constants.RESULT_FOLDER;
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID + " DESC";
        Cursor c = context.getContentResolver().query(uri, projection, null, null, orderBy);
        int vidsCount = 0;
        if (c != null) {
            c.moveToFirst();
            vidsCount = c.getCount();

            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                String path = c.getString(0);
                String name = getVideoName(path);
                int id = c.getInt(1);
                if (path.contains(folder)) {
                    Photo video = new Photo(id, path, name, 0);
                    videos.add(video);
                }
            }
            c.close();
        }
        Log.d("ttt", "Total count of videos: " + vidsCount);
        return videos;
    }

    private static String getVideoName(String path) {
        String[] names = path.split("/");
        return names[names.length - 1];
    }

    @NotNull
    @Override
    public List<Photo> loadList() {
        FileUtils.Companion.reloadMedia(context);
        return dumpVideos();
    }
}
