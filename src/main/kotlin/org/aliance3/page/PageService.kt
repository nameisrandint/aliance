package org.aliance3.page

import org.aliance3.common.KeyValueStorage
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

const val KEY = "last.updated.at"

@Service
@EnableScheduling
class PageService(
    private val kv: KeyValueStorage,
    private val imageDao: ImageDao,
    private val telegram: Telegram
) {

    @Scheduled(cron = "0 30 8,12,16,20 * * ?")
    fun sentNextImage() {
        return sentImageWithSwipe(1)
    }

    fun sentImageWithSwipe(swipe: Int) {
        val id = moveCursorOrIfNullSetToStart(swipe)
        val file = imageDao.selectImageById(id)
        telegram.sendImage(file!!)
    }

    fun moveCursorOrIfNullSetToStart(step: Int): Int {
        var curId = kv.getInt(KEY)
        curId = if (curId == null) {
            imageDao.selectFirstImageId()
        } else {
            imageDao.selectImageIdFromCurrent(curId, step)
        }

        if (curId == null) {
            curId = imageDao.selectFirstImageId()
        }

        kv.set(KEY, curId.toString())
        return curId!!
    }
}