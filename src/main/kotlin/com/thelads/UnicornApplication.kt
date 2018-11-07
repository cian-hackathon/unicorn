package com.thelads

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
open class UnicornApplication

fun main(args: Array<String>) {
    runApplication<UnicornApplication>(*args)
}

