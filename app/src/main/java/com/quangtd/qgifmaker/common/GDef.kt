package com.quangtd.qgifmaker.common

import android.os.Environment
import com.quangtd.qgifmaker.util.TimeUtils
import java.io.File

/**
 * Created by QuangTD
 * on 1/14/2018.
 */
class GDef {
    companion object {
        //permission
        const val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: Int = 100
        const val PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: Int = 101

        //request code
        const val REQUEST_CODE_CHOOSE_MUSIC: Int = 200


        //constant
        const val DELAY_ON_CLICK = 500
        const val DEFAULT_FONT = "SerreriaSobria.otf"
        const val FPS = 30
        var PREVIEW_DIMEN = 0
        const val ONE_SECOND = 1000L
        const val DELAY_RENDER: Float = 1.01f
        const val MILLISECONDS_PER_FRAME = ONE_SECOND / FPS
        const val SECONDS_PER_FRAME = 1f / FPS
        const val COMPLETED = "completed"

        //environment
        val SDCARD_PATH = Environment.getExternalStorageDirectory().absolutePath
        var DATA_PATH = "" // init in MainApplication
        var DATA_PATH_AUDIO = ""
        var DATA_PATH_EFFECT = ""
        var DATA_PATH_THEME = ""
        var DATA_PATH_STICKER = ""
        var DATA_PATH_VIDEO_FRAME = ""
        var DATA_PATH_TRANSITION = ""

        const val TEMP_FOLDER = ".temp"
        const val DEFAULT_THEME = "heart_pink"
        var TEMP_FOLDER_PATH = ""
        var TEMP_PICTURE_FORMAT = ""
        var TEMP_AUDIO_FORMAT = ""
        var TEMP_VIDEO_FORMAT = ""

        const val NAME_NONE = "none"

        private const val OUTPUT_FOLDER = "QStudio"
        val OUTPUT_FOLDER_PATH = "$SDCARD_PATH/$OUTPUT_FOLDER"
        private val OUTPUT_VIDEO_FORMAT = "$OUTPUT_FOLDER_PATH/QStudio_%s.mp4"
        val OUTPUT_PICTURE_FORMAT = "$OUTPUT_FOLDER_PATH/QStudio_%s.png"
        val OUTPUT_AUDIO_FORMAT = "$OUTPUT_FOLDER_PATH/QStudio_%s.aac"

        fun getDefaultOutput(): String {
            return String.format(OUTPUT_VIDEO_FORMAT, TimeUtils.parseTimestampToString(System.currentTimeMillis()))
        }

        fun getDefaultTempFile(extension: String): String {
            return when (extension) {
                TYPE_PNG -> String.format(TEMP_PICTURE_FORMAT, TimeUtils.parseTimestampToString(System.currentTimeMillis()))
                TYPE_AAC, TYPE_MP3, TYPE_M4A -> String.format(TEMP_AUDIO_FORMAT, TimeUtils.parseTimestampToString(System.currentTimeMillis()), extension)
                TYPE_MP4 -> String.format(TEMP_VIDEO_FORMAT, TimeUtils.parseTimestampToString(System.currentTimeMillis()))
                else -> "$TEMP_FOLDER_PATH${File.separator}.temp_${TimeUtils.parseTimestampToString(System.currentTimeMillis())}.$extension"
            }
        }

        const val TYPE_MP4 = "mp4"
        const val TYPE_AAC = "aac"
        const val TYPE_MP3 = "mp3"
        const val TYPE_M4A = "m4a"
        const val TYPE_PNG = "png"

//        fun getDefaultAudio(): Music {
//            return Music(".music_romantic1.aac", GDef.DATA_PATH_AUDIO + "/.music_romantic1.aac", "", 84000)
//        }

        fun getDefaultEffect(): String {
            return GDef.DATA_PATH_EFFECT + "/.effect6.jpg"
        }


        const val DAY_TIME_FORMAT = "dd-MM-yyyy-hh-mm-ss"

        //key bundle
        const val KEY_BUNDLE_LIST_PHOTO = "KEY_BUNDLE_LIST_PHOTO"
        const val KEY_BUNDLE_PHOTO_ID = "KEY_BUNDLE_PHOTO_ID"
        const val KEY_BUNDLE_MUSIC = "KEY_BUNDLE_MUSIC"
        const val KEY_BUNDLE_VIDEO_OUTPUT = "KEY_BUNDLE_VIDEO_OUTPUT"
        const val KEY_BUNDLE_TAB_STICKER = "KEY_BUNDLE_TAB_STICKER"
        const val KEY_BUNDLE_STICKER = "KEY_BUNDLE_STICKER"
        const val KEY_BUNDLE_SUBTITLE = "KEY_BUNDLE_SUBTITLE"
        const val KEY_BUNDLE_SUBTITLE_TYPE: String = "KEY_BUNDLE_SUBTITLE_TYPE"
        const val KEY_BUNDLE_TYPE_EDIT: String = "KEY_BUNDLE_TYPE_EDIT"
        const val KEY_BUNDLE_TYPE_ADD: String = "KEY_BUNDLE_TYPE_ADD"
        //key share_pref
        const val KEY_SHARE_PREF_DEFAULT_MODE = "KEY_SHARE_PREF_DEFAULT_MODE"
        const val PREF_DARK_THEME ="pref_dark_theme"
        const val KEY_BUNDLE_NEW_VIDEO = "KEY_BUNDLE_NEW_VIDEO"


    }
}