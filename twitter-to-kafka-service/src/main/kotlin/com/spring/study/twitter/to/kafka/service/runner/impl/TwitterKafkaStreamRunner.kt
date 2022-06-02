package com.spring.study.twitter.to.kafka.service.runner.impl

import com.spring.study.twitter.to.kafka.service.config.TwitterToKafkaServiceConfigData
import com.spring.study.twitter.to.kafka.service.listener.TwitterKafkaStatusListener
import com.spring.study.twitter.to.kafka.service.runner.StreamRunner
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import twitter4j.FilterQuery
import twitter4j.TwitterStream
import twitter4j.TwitterStreamFactory
import javax.annotation.PreDestroy

//@Component
//@ConditionalOnProperty(name = ["twitter-to-kafka-service.enable-mock-tweets"], havingValue = "false", matchIfMissing = true)
class TwitterKafkaStreamRunner(
    val configData: TwitterToKafkaServiceConfigData,
    val twitterKafkaStatusListener: TwitterKafkaStatusListener
) {
    private val logger = LoggerFactory.getLogger(TwitterKafkaStreamRunner::class.java)

    private lateinit var  twitterStream: TwitterStream
    fun start() {
        twitterStream = TwitterStreamFactory.getSingleton()
        twitterStream.addListener(twitterKafkaStatusListener)
        addFilter()
    }

    @PreDestroy
    fun shutDown(){
        logger.info("Closed twitter stream")
        twitterStream.shutdown()
    }

    private fun addFilter(){
        val keywords = configData.twitterKeyWords
        val filterQuery = FilterQuery(*keywords.toTypedArray())
        twitterStream.filter(filterQuery)
        logger.info("Started filter twitter keyword ${keywords.joinToString(",")}")
    }
}