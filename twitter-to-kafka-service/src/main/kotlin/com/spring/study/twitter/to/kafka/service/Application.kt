package com.spring.study.twitter.to.kafka.service

import com.spring.study.twitter.to.kafka.service.config.TwitterToKafkaServiceConfigData
import com.spring.study.twitter.to.kafka.service.runner.StreamRunner
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application: CommandLineRunner {
    private val logger = LoggerFactory.getLogger(Application::class.java)

    @Autowired
    private lateinit var twitterToKafkaServiceConfigData: TwitterToKafkaServiceConfigData

    @Autowired
    private lateinit var streamRunner: StreamRunner

    override fun run(vararg args: String?) {
        logger.info("App starts....")
        logger.info(twitterToKafkaServiceConfigData.twitterKeyWords.joinToString(","))
        logger.info(twitterToKafkaServiceConfigData.welcomeMessage)
        streamRunner.start()
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}