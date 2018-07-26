package com.saskcow.bowling

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BowlingApplication

fun main(args: Array<String>) {
    runApplication<BowlingApplication>(*args)
}
