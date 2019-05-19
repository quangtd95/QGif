package com.quangtd.qgifmaker.util

import com.quangtd.qgifmaker.common.GDef
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by QuangTD
 * on 1/21/2018.
 */
class TimeUtils {
    companion object {
        fun parseTimestampToString(timeStamp: Long, format: String): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeStamp
            val dateFormat = SimpleDateFormat(format, Locale.getDefault())
            return dateFormat.format(calendar.time)
        }

        fun parseTimestampToString(timeStamp: Long): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeStamp
            val dateFormat = SimpleDateFormat(GDef.DAY_TIME_FORMAT, Locale.getDefault())
            return dateFormat.format(calendar.time)
        }

        /**
         * input: seconds
         * format : 00:00
         */
        fun parseSecondsTimeToString(duration: Float): String {
            if (duration > 0) {
                val minute = (duration / 60).toInt()
                val second = (duration % 60).toInt()
                return if (minute >= 10) "$minute" else "0$minute" + ":" + if (second >= 10) "$second" else "0$second"
            }
            return "00:00"
        }

        fun parseMilliSecondsTimeToString(time: Long): String {
            return parseSecondsTimeToString((time / 1000).toFloat())
        }

        /**
         * input : 00:00:00
         * output: seconds float
         */
        fun parseTimeStringToFloat(s: String): Float {
            return try {
                val s1 = s.split(regex = ":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                s1[0].toFloat() * 3600 + s1[1].toFloat() * 60 + s1[2].toFloat()
            } catch (ex: Exception) {
                0f
            }
        }
    }
}