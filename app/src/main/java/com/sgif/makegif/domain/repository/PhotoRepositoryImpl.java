package com.sgif.makegif.domain.repository;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.sgif.makegif.domain.model.FolderMedia;
import com.sgif.makegif.domain.model.Media;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public class PhotoRepositoryImpl implements MediaRepository {

    @Override
    public List<FolderMedia> getFolderMedia(Context context) {
        List<Media> mPhotos = new ArrayList<>();

        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID + " DESC";
        Cursor cc = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);

        assert cc != null;
        cc.moveToFirst();
        for (int i = 0; i < cc.getCount(); i++) {
            cc.moveToPosition(i);
            int id = cc.getInt(cc.getColumnIndex(MediaStore.MediaColumns._ID));

            Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
            String name = cc.getString(1);
            String path = cc.getString(0);

            Media photo = new Media();
            photo.setId(id);
            photo.setUri(uri);
            photo.setPath(path);
            photo.setName(name);
            mPhotos.add(photo);
        }
        cc.close();
        return new ArrayList<>(getAlbumImage(mPhotos));
    }

    private List<FolderMedia> getAlbumImage(List<Media> photos) {
        List<FolderMedia> folderPhotos = new ArrayList<>();

        for (Media photo : photos) {
            if (!checkAlbum(folderPhotos, photo)) {
                List<Media> imagesAlbum = new ArrayList<>();
                imagesAlbum.add(photo);
                FolderMedia folderPhoto = new FolderMedia(imagesAlbum, getAlbumName(photo.getPath()));
                folderPhoto.setPath(getPathAlbum(photo.getPath()));
                folderPhotos.add(folderPhoto);
            }
        }
        return folderPhotos;
    }

    private String getAlbumName(String path) {
        String[] names = path.split("/");
        return names[names.length - 2];
    }

    private String getPathAlbum(String path) {
        String[] names = path.split("/");
        Log.d("ttt", "getPathAlbum: " + path + " : " + names.length + " : ");
        return path.substring(0, path.length() - names[names.length - 1].length());
    }

    private boolean checkAlbum(List<FolderMedia> folderPhotos, Media photo) {
        boolean check = false;
        for (int i = 0; i < folderPhotos.size(); i++) {
            if (getAlbumName(photo.getPath()).equals(folderPhotos.get(i).getName())) {
                check = true;
                folderPhotos.get(i).getListMedia().add(photo);
                break;
            }
            check = false;
        }
        return check;
    }

}
