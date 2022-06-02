package com.spring.study.twitter.to.kafka.service.exception

class TwitterToKafkaServiceException(message: String?): RuntimeException() {
    override val message: String? = message
}