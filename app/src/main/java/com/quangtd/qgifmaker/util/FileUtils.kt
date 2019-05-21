package com.quangtd.qgifmaker.util

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.File


/**
 * Created by QuangTD
 * on 1/29/2018.
 */
class FileUtils {
    companion object {
        fun getListFileInFolder(path: String): List<String> {
            val list = ArrayList<String>()
            val directory = File(path)
            if (!directory.exists()) return list
            val files = directory.listFiles()
            if (files == null || files.isEmpty()) list
            files.indices.mapTo(list) { files[it].absolutePath }
            return list
        }

        fun getDurationAudio(uri: Uri, context: Context): Int {
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(context, uri)
            val durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            return Integer.parseInt(durationStr)
        }

        fun reloadMedia(context: Context) {
            MediaScannerConnection.scanFile(context.applicationContext,
                    arrayOf(Environment.getExternalStorageDirectory().toString()),
                    null
            ) { path, uri ->
                Log.i("ExternalStorage", "Scanned $path:")
                Log.i("ExternalStorage", "-> uri=$uri")
            }

        }

        fun reloadMedia(context: Context, path: String?) {
            var path2 = path
            path2 ?: run { path2 = Environment.getExternalStorageDirectory().toString() }
            MediaScannerConnection.scanFile(context.applicationContext,
                    arrayOf(path2),
                    null
            ) { path1, uri ->
                Log.i("ExternalStorage", "Scanned $path1:")
                Log.i("ExternalStorage", "-> uri=$uri")
            }
        }

        fun delete(f: File) {
            if (f.isDirectory) {
                for (c in f.listFiles()) {
                    delete(c)
                }
            } else {
                f.delete()
            }
        }
    }

}