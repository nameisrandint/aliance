package org.aliance3

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DelmeController {

    @GetMapping("/delme")
    fun delme(): Prop {
        return Prop("delmeprop")
    }
}

data class Prop (
    val prop: String
    )