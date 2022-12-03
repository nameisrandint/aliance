package org.aliance3.common

import org.springframework.stereotype.Component
import java.io.File

@Component
class CredentialsProvider {

    fun load() : Map<String, String> {
        val pathToCred = System.getenv("path_to_credentials_file")
        return File(pathToCred).readLines().asSequence()
            .filter { it != "" }
            .map { it.split("=") }
            .map { it[0].trim() to it[1].trim() }
            .toMap()
    }
}