package com.spring.study.twitter.to.kafka.service.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("twitter-to-kafka-service")
class TwitterToKafkaServiceConfigData {
    lateinit var twitterKeyWords: List<String>
    lateinit var welcomeMessage: String
}