package org.aliance3

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * TODO:
 * 1. logging
 * 2. Unable to deserialize response
 * 3. remove tmp files
 * 4. NPE on startup when using script
 * 5. завести тестового бота
 * 6. cors
 */

@SpringBootApplication
class Aliance3Application

fun main(args: Array<String>) {
    runApplication<Aliance3Application>(*args)
}
