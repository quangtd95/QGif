package com.sgif.makegif.domain.model;

import com.sgif.makegif.common.Constants;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public enum MediaType {
    PHOTO(Constants.TYPE_PHOTO),
    VIDEO(Constants.TYPE_VIDEO);

    private int code;

    MediaType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MediaType getByCode(int code) {
        for (MediaType mediaType : values()) {
            if (mediaType.code == code) return mediaType;
        }
        throw new IllegalArgumentException();

    }
}
