package org.aliance3

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.system.exitProcess

@RestController
class PingController {

    val log = LoggerFactory.getLogger(PingController::class.java)

    @GetMapping("/ping")
    fun ping(): Int {
        log.info("that's the ping")
        return 0
    }

    @GetMapping("/kill")
    fun kill() {
        exitProcess(0)
    }

}