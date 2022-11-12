package org.aliance3.alliance

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.ForkJoinPool
import kotlin.system.exitProcess

@RestController
class SuperPingController {

    val log: Logger = LoggerFactory.getLogger(SuperPingController::class.java)

    @GetMapping("/ping")
    fun ping(): Int {
        log.info("that's the ping")
        return 0
    }

    @GetMapping("/kill")
    fun kill(): String {
        ForkJoinPool.commonPool().submit {
            Thread.sleep(100)
            exitProcess(0)
        }
        return "All fine, shutting down...\n"
    }

}