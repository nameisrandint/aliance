package org.aliance3.page

import org.aliance3.common.KeyValueStorage
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

const val CURSOR = "last.updated.at"
const val COMMIT = "committed"

@Service
@EnableScheduling
class PageService(
    private val kv: KeyValueStorage,
    private val imageDao: ImageDao,
    private val telegram: Telegram
) {

    @Scheduled(cron = "0 30 8,12,16,20 * * ?")
    fun nextNotification() {
        val committed = kv.getBool(COMMIT)
        if (committed) {
            sentImageWithSwipe(1)
            removeCommit()
        } else {
            telegram.sentText("Йо мэээн, шо делаешь?")
        }
    }

    fun setCursor(pos: Int) {
        kv.setInt(CURSOR, pos)
    }

    fun commit() {
        kv.setTrue(COMMIT)
    }

    fun removeCommit() {
        kv.setFalse(COMMIT)
    }

    fun sentImageWithSwipe(swipe: Int) {
        val id = moveCursorOrIfNullSetToStart(swipe)
        val file = imageDao.selectImageById(id)
        telegram.sendImage(file!!)
    }

    fun moveCursorOrIfNullSetToStart(step: Int): Int {
        var curId = kv.getInt(CURSOR)

        curId = if (curId == null) {
            imageDao.selectFirstImageId()
        } else {
            imageDao.selectImageIdFromCurrent(curId, step)
        }

        if (curId == null) {
            curId = imageDao.selectFirstImageId()
        }

        kv.set(CURSOR, curId.toString())
        return curId!!
    }
}