package tech.loucianus.im.service.impl

import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import tech.loucianus.im.model.dto.MessageGetter
import tech.loucianus.im.model.dto.MessageSender
import tech.loucianus.im.model.entity.Message
import tech.loucianus.im.repository.MessageRepository
import tech.loucianus.im.service.ChatService
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@Service
class ChatServiceImpl: ChatService {

    companion object {
        private val log = LogFactory.getLog(this::class.java)
    }

    @Autowired private lateinit var simpMessagingTemplate: SimpMessagingTemplate

    @Autowired private lateinit var messageRepository: MessageRepository

    override fun sentToUser(message: MessageGetter) {

        // 完整message信息
        val messageSender = MessageSender(
            from_id = message.from_id,
            to_id = message.to_id,
            name =message.name,
            data = message.data,
            date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( Date() ),
            type = message.type
        )

        if (log.isInfoEnabled) {
            log.info("message:$message")
            log.info("message:$messageSender")
            log.info("to_user::${message.to_email}")
        }

        messageRepository.saveMessage(
            Message(
                id = 0,
                fromId = messageSender.from_id,
                toId = messageSender.to_id,
                content = messageSender.data,
                type = "s",
                date = Timestamp(System.currentTimeMillis())
            )
        )

        // 第一参数表示接收信息的用户，第二个是浏览器订阅的地址，第三个是消息本身
        simpMessagingTemplate.convertAndSendToUser(message.to_email, "/topic/private", messageSender)
    }


    override fun getChatFile(id: Int, uid: Int): List<Message> {

        val messageList = messageRepository.findMessage(id, uid)

        if (log.isInfoEnabled) log.info("messageList::$messageList")
        return messageList
    }

}