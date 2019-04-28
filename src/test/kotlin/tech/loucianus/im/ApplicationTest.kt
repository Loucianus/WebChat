package tech.loucianus.im

import org.apache.commons.logging.LogFactory
import org.jasypt.encryption.StringEncryptor
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import tech.loucianus.im.repository.MessageRepository
import java.lang.Exception


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ApplicationTest {

    companion object {
        private val log = LogFactory.getLog(this::class.java)
    }

    @Autowired
    lateinit var cipher: StringEncryptor

    @Autowired
    lateinit var messageRepository: MessageRepository

    @Test
    fun contextLoads(){
    }

    @Test
    @Throws(Exception::class)
    fun encodePassword(){
        val password = "loucianus"
        println(cipher.encrypt(password))
    }
}