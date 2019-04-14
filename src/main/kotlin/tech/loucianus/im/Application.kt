package tech.loucianus.im

import org.apache.commons.logging.LogFactory.getLog
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.PropertySource
import tech.loucianus.im.util.CipherUtil.setJasyptPassword

@EnableCaching
@SpringBootApplication
@MapperScan(
    value = ["tech.loucianus.im.repository"]
)
@PropertySource(
    encoding = "UTF-8",
    value = [
        "value.properties"
    ]
)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
    setJasyptPassword()
    getLog(Application::class.java).info("Instant Messaging Started.")
}