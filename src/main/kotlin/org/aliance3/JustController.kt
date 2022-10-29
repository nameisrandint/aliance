package org.aliance3

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.*

@Controller
class JustController {

    @Autowired
    private lateinit var alianceDao: AlianceDao

    @GetMapping("/gettrackforuser/{userId}")
    fun pageState(@PathVariable userId: Int): ResponseEntity<PageState> {
        val res = alianceDao.selectPageState(userId)
        return ResponseEntity.of(Optional.of(res))
    }
}