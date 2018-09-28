package com.sgif.makegif.domain.repository;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.sgif.makegif.domain.model.FolderImage;
import com.sgif.makegif.domain.model.Photo;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public class ImageRepositoryImpl implements ImageRepository {

    @Override
    public List<FolderImage> getFolderImages(Context context) {
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
            photo.setUri(uri);
            photo.setPath(path);
            photo.setName(name);
            mPhotos.add(photo);
        }
        cc.close();
        return new ArrayList<>(getAlbumImage(mPhotos));
    }

    private List<FolderImage> getAlbumImage(List<Photo> photos) {
        List<FolderImage> folderImages = new ArrayList<>();

        for (Photo photo : photos) {
            if (!checkAlbum(folderImages, photo)) {
                List<Photo> imagesAlbum = new ArrayList<>();
                imagesAlbum.add(photo);
                FolderImage folderImage = new FolderImage(imagesAlbum, getAlbumName(photo.getPath()));
                folderImage.setPath(getPathAlbum(photo.getPath()));
                folderImages.add(folderImage);
            }
        }
        return folderImages;
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

    private boolean checkAlbum(List<FolderImage> folderImages, Photo photo) {
        boolean check = false;
        for (int i = 0; i < folderImages.size(); i++) {
            if (getAlbumName(photo.getPath()).equals(folderImages.get(i).getName())) {
                check = true;
                folderImages.get(i).getPhotos().add(photo);
                break;
            }
            check = false;
        }
        return check;
    }

}
