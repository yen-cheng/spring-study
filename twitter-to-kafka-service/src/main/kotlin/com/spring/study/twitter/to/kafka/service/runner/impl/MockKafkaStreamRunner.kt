package com.spring.study.twitter.to.kafka.service.runner.impl

import com.spring.study.twitter.to.kafka.service.config.TwitterToKafkaServiceConfigData
import com.spring.study.twitter.to.kafka.service.exception.TwitterToKafkaServiceException
import com.spring.study.twitter.to.kafka.service.listener.TwitterKafkaStatusListener
import com.spring.study.twitter.to.kafka.service.runner.StreamRunner
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import twitter4j.Status
import twitter4j.TwitterException
import twitter4j.TwitterObjectFactory
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ThreadLocalRandom


private val WORDS = arrayOf(
    "1111",
    "2222",
    "3333",
    "4444",
    "5555",
    "6666",
    "7777",
    "8888",
    "9999"
)
private const val TWITTER_STATUS_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy"

@Component
//@ConditionalOnProperty(name = ["twitter-to-kafka-service.enable-mock-tweets"], havingValue = "true")
class MockKafkaStreamRunner(
    private val twitterToKafkaServiceConfigData: TwitterToKafkaServiceConfigData,
    private val twitterKafkaStatusListener: TwitterKafkaStatusListener
) : StreamRunner {

    private val logger = LoggerFactory.getLogger(MockKafkaStreamRunner::class.java)
    private val twitterAsRowJson = "{\"created_at\":\"{0}\", \"id\":\"{1}\",\"text\":\"{2}\", \"user\":{\"id\":\"{3}\"}}"
    private val random = Random()
    override fun start() {
        val keywords = twitterToKafkaServiceConfigData.twitterKeyWords.toTypedArray()
        val minTweetLength = twitterToKafkaServiceConfigData.mockMinTweetLength
        val maxTweetLength = twitterToKafkaServiceConfigData.mockMaxTweetLength
        val sleepTimeMs: Long = twitterToKafkaServiceConfigData.mockSleepMs
        logger.info("Starting mock filtering twitter streams for keywords {}", keywords.contentToString())
        simulateTwitterStream(keywords, minTweetLength, maxTweetLength, sleepTimeMs)
    }

    private fun simulateTwitterStream(
        keywords: Array<String>,
        minTweetLength: Int,
        maxTweetLength: Int,
        sleepTimeMs: Long
    ) {
        Executors.newSingleThreadExecutor().submit {
            try {
                while (true) {
                    val formattedTweetAsRawJson: String = getFormattedTweet(keywords, minTweetLength, maxTweetLength)!!
                    val status: Status = TwitterObjectFactory.createStatus(formattedTweetAsRawJson)
                    twitterKafkaStatusListener.onStatus(status)
                    sleep(sleepTimeMs)
                }
            } catch (e: TwitterException) {
                logger.error("Error creating twitter status!", e)
            }
        }
    }

    private fun sleep(sleepTimeMs: Long) {
        try {
            Thread.sleep(sleepTimeMs)
        } catch (e: InterruptedException) {
            throw TwitterToKafkaServiceException("Error while sleeping for waiting new status to create!!")
        }
    }

    private fun getFormattedTweet(keywords: Array<String>, minTweetLength: Int, maxTweetLength: Int): String? {
        val params = arrayOf(
            ZonedDateTime.now().format(DateTimeFormatter.ofPattern(TWITTER_STATUS_DATE_FORMAT, Locale.ENGLISH)),
            ThreadLocalRandom.current().nextLong(
                Long.MAX_VALUE
            ).toString(),
            getRandomTweetContent(keywords, minTweetLength, maxTweetLength),
            ThreadLocalRandom.current().nextLong(Long.MAX_VALUE).toString()
        )
        return formatTweetAsJsonWithParams(params)
    }

    private fun formatTweetAsJsonWithParams(params: Array<String>): String? {
        var tweet: String = twitterAsRowJson
        for (i in params.indices) {
            tweet = tweet.replace("{$i}", params[i])
        }
        return tweet
    }

    private fun getRandomTweetContent(keywords: Array<String>, minTweetLength: Int, maxTweetLength: Int): String {
        val tweet = StringBuilder()
        val tweetLength: Int = random.nextInt(maxTweetLength - minTweetLength + 1) + minTweetLength
        return constructRandomTweet(keywords, tweet, tweetLength)
    }

    private fun constructRandomTweet(keywords: Array<String>, tweet: StringBuilder, tweetLength: Int): String {
        for (i in 0 until tweetLength) {
            tweet.append(WORDS[random.nextInt(WORDS.size)]).append(" ")
            if (i == tweetLength / 2) {
                tweet.append(keywords[random.nextInt(keywords.size)]).append(" ")
            }
        }
        return tweet.toString().trim { it <= ' ' }
    }
}