package com.spring.study.twitter.to.kafka.service

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application: CommandLineRunner {
    override fun run(vararg args: String?) {
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}