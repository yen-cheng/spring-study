package com.spring.study.twitter.to.kafka.service.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import kotlin.properties.Delegates

@Configuration
@ConfigurationProperties("twitter-to-kafka-service")
class TwitterToKafkaServiceConfigData {
    lateinit var twitterKeyWords: List<String>
    lateinit var welcomeMessage: String
    var mockMinTweetLength by Delegates.notNull<Int>()
    var mockMaxTweetLength by Delegates.notNull<Int>()
    var mockSleepMs by Delegates.notNull<Long>()
}