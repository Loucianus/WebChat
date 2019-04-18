package tech.loucianus.im.service.impl

import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import tech.loucianus.im.model.vo.MessageGetter
import tech.loucianus.im.model.vo.MessageSender
import tech.loucianus.im.model.dao.GroupMessage
import tech.loucianus.im.model.po.Message
import tech.loucianus.im.repository.MessageRepository
import tech.loucianus.im.service.ChatService
import tech.loucianus.im.util.Utils
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
            name = message.name,
            content = message.data,
            date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),
            type = message.type
        )

        if (log.isInfoEnabled) {
            log.info("message:$message")
            log.info("messageSender:$messageSender")
        }


        messageRepository.saveMessage(
            Message(
                id = 0,
                fromId = messageSender.from_id,
                toId = messageSender.to_id,
                content = messageSender.content,
                type = "s",
                date = Utils.getNowTimestamp(),
                target = if (messageSender.from_id > messageSender.to_id) {
                            "${messageSender.from_id}#${messageSender.to_id}"
                        } else {
                            "${messageSender.to_id}#${messageSender.from_id}"
                        },
                isRead = "f"
            )
        )
        // 第一参数表示接收信息的用户，第二个是浏览器订阅的地址，第三个是消息本身
        simpMessagingTemplate.convertAndSendToUser(message.to_email, "/topic/private", messageSender)
    }

    override fun sentToGroup(message: MessageGetter): GroupMessage {

        if (log.isInfoEnabled) log.info("message:$message")

        messageRepository.saveMessage(
            Message(
                id = 0,
                fromId = message.from_id,
                toId = 0,
                content = message.data,
                type = "s",
                date = Utils.getNowTimestamp(),
                target = "${message.from_id}#0",
                isRead = "t"
            )
        )

        return GroupMessage(
            fromId = message.from_id,
            content = message.data,
            type = message.type,
            date = Timestamp(System.currentTimeMillis()),
            name = message.name
        )
    }

    override fun getChatFile(id: Int, uid: Int): List<Message> {

        val messageList = messageRepository.findMessage(id, uid)

        if (log.isInfoEnabled) log.info("messageList::$messageList")

        return messageList
    }

    override fun getGroupChatFile(): List<GroupMessage> {
        val messageList = messageRepository.findGroupMessage()

        if (log.isInfoEnabled) log.info("messageList::$messageList")

        return messageList
    }
}