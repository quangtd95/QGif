package com.sgif.makegif.domain.task;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler;
import nl.bravobit.ffmpeg.FFmpeg;

/**
 * Created by quang.td95@gmail.com
 * on 10/6/2018.
 */
public class ExportGifFfmpeg {
    private OnExportGifCallback callback;
    private FFmpeg ffmpeg;

    public ExportGifFfmpeg(FFmpeg ffmpeg, OnExportGifCallback callback) {
        this.callback = callback;
        this.ffmpeg = ffmpeg;
    }

    public void execute(ExportGifParams params) {
        convertVideoToGif(params);
    }

    private void convertVideoToGif(ExportGifParams params) {
        String cmd;
        if (params.getDelay() == 0) {
            cmd = String.format(
                    Locale.getDefault(),
                    "-ss %s -i %s -t %s -vf scale=%d:%d %s -hide_banner",
                    convertMillisecondToString(params.getStartTime()),
                    params.getVideo().getPath(),
                    convertMillisecondToString(params.getEndTime() - params.getStartTime()),
                    params.getWidth(),
                    params.getHeight(),
                    params.getResultPath());
        } else {
            cmd = String.format(
                    Locale.getDefault(),
                    "-ss %s -i %s -r %d -t %s -vf scale=%d:%d %s -hide_banner",
                    convertMillisecondToString(params.getStartTime()),
                    params.getVideo().getPath(),
                    convertDelayToFPS(params.getDelay()),
                    convertMillisecondToString(params.getEndTime() - params.getStartTime()),
                    params.getWidth(),
                    params.getHeight(),
                    params.getResultPath());
        }
        Log.e("TAGG", cmd);
        long totalDuration = params.getEndTime() - params.getStartTime();
        long start = System.currentTimeMillis();
        ffmpeg.execute(buildCommand(cmd), new ExecuteBinaryResponseHandler() {

            @Override
            public void onStart() {
                Log.e("TAGG", "onstart");
                callback.onPrepareExportGif();
            }

            @Override
            public void onProgress(String message) {
                String timeLog = getTimeInFFmpegLog(message);
                float progress = convertStringToMillisecond(timeLog) * 1.0f / totalDuration;
                callback.onProgressExportGif(progress);
                Log.e("TAGG", message);
            }

            @Override
            public void onFailure(String message) {
                Log.e("TAGG", message);
                callback.onCancelledExportGif(message);
            }

            @Override
            public void onSuccess(String message) {
                Log.e("TAGG", message);
                callback.onCompleteExportGif(params.getResultPath());
            }

            @Override
            public void onFinish() {
                Log.e("TAGG", "finish " + (System.currentTimeMillis() - start));
            }

        });
    }

    private int convertDelayToFPS(int delay) {
        return (int) (TimeUnit.SECONDS.toMillis(1L) / delay);
    }

    private String[] buildCommand(String cmd) {
        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(cmd);
        while (m.find()) list.add(m.group(1).replace("\"", ""));
        String[] cmdList = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            cmdList[i] = list.get(i);
        }

        return cmdList;
    }

    /**
     * @return 00:01:03.250
     */
    private String convertMillisecondToString(long milliseconds) {
        int hour = (int) TimeUnit.MILLISECONDS.toHours(milliseconds);
        long rest = milliseconds - TimeUnit.HOURS.toMillis(hour);
        int minute = (int) TimeUnit.MILLISECONDS.toMinutes(rest);
        rest = rest - TimeUnit.MINUTES.toMillis(minute);
        int second = (int) TimeUnit.MILLISECONDS.toSeconds(rest);
        rest = rest - TimeUnit.SECONDS.toMillis(second);
        int milli = (int) rest;
        return String.format(Locale.US, "%02d:%02d:%02d.%03d",
                hour, minute, second, milli
        );
    }

    private long convertStringToMillisecond(String s) {
        try {
            String[] sSplited = s.split(":");
            int hours = Integer.parseInt(sSplited[0]);
            int minutes = Integer.parseInt(sSplited[1]);
            int seconds = Integer.parseInt(sSplited[2].split("\\.")[0]);
            int milliseconds = Integer.parseInt(sSplited[2].split("\\.")[1]);
            return TimeUnit.HOURS.toMillis(hours)
                    + TimeUnit.MINUTES.toMillis(minutes)
                    + TimeUnit.SECONDS.toMillis(seconds)
                    + TimeUnit.MILLISECONDS.toMillis(milliseconds);
        } catch (Exception e) {
            Log.e("TAGG", e.toString());
            return 0;
        }
    }

    private String getTimeInFFmpegLog(String log) {
        int indexOfTime = log.indexOf("time=0");
        if (indexOfTime == -1) {
            return "00:00:00.00";
        }
        return log.substring(indexOfTime + 5, indexOfTime + 5 + 11);
    }
}
