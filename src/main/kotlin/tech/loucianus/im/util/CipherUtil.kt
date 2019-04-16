package tech.loucianus.im.util

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Component

@Component
object CipherUtil {

    private val log: Log = LogFactory.getLog(this::class.java)

    private const val JASYPT_ENCRYPTOR_KEY: String = "nYU1oHQxE9Fv6BtPueUC+g=="

    fun setJasyptPassword() {
        System.setProperty("jasypt.encryptor.password", JASYPT_ENCRYPTOR_KEY)
        if (log.isInfoEnabled) log.info("Jasypt Encryptor Password Configuration Completed.")
    }
}