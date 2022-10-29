package org.aliance3

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class JustController {

    @Autowired
    private lateinit var alianceDao: AlianceDao

    @GetMapping("/gettrackforuser/{userId}")
    fun pageState(@PathVariable userId: Int): PageState {
        val res = alianceDao.selectPageState(userId)
        return res
    }

    @PostMapping("/markascompleted")
    fun markAsCompleted(
        @RequestParam subTaskId: Int
    ) {
        alianceDao.markSubTaskAsCompleted(subTaskId)
        val cardId = alianceDao.cardIdBySubTask(subTaskId)

        if (alianceDao.isAnySubTaskInProgress(cardId)) {
            alianceDao.updateCardStatus(cardId, 2)
        }

        if (alianceDao.isAllSubTasksDone(cardId)) {
            alianceDao.updateCardStatus(cardId, 3)
        }

    }
}