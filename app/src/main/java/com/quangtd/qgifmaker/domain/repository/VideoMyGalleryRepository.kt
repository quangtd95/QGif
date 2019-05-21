package com.quangtd.qgifmaker.domain.repository

import com.quangtd.qgifmaker.domain.model.Photo

/**
 * Created by QuangTD
 * on 3/14/2018.
 **/
interface VideoMyGalleryRepository {
    fun loadList(): List<Photo>
}