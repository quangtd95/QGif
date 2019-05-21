package com.quangtd.qgifmaker.domain.repository;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.quangtd.qgifmaker.domain.model.FolderPhoto;
import com.quangtd.qgifmaker.domain.model.Photo;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public class PhotoRepositoryImpl implements PhotoRepository {

    @Override
    public List<FolderPhoto> getFolderMedia(Context context) {
        List<Photo> mPhotos = new ArrayList<>();

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

            Photo photo = new Photo();
            photo.setId(id);
            photo.setUri(uri);
            photo.setPath(path);
            photo.setName(name);
            mPhotos.add(photo);
        }
        cc.close();
        return new ArrayList<>(getAlbumImage(mPhotos));
    }

    private List<FolderPhoto> getAlbumImage(List<Photo> photos) {
        List<FolderPhoto> folderPhotos = new ArrayList<>();

        for (Photo photo : photos) {
            if (!checkAlbum(folderPhotos, photo)) {
                List<Photo> imagesAlbum = new ArrayList<>();
                imagesAlbum.add(photo);
                FolderPhoto folderPhoto = new FolderPhoto(imagesAlbum, getAlbumName(photo.getPath()));
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

    private boolean checkAlbum(List<FolderPhoto> folderPhotos, Photo photo) {
        boolean check = false;
        for (int i = 0; i < folderPhotos.size(); i++) {
            if (getAlbumName(photo.getPath()).equals(folderPhotos.get(i).getName())) {
                check = true;
                folderPhotos.get(i).getPhotos().add(photo);
                break;
            }
            check = false;
        }
        return check;
    }

}
