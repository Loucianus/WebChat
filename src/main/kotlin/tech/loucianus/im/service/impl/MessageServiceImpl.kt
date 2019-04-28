package tech.loucianus.im.service.impl

import com.github.pagehelper.Page
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tech.loucianus.im.model.dao.GroupMessage
import tech.loucianus.im.model.po.Message
import tech.loucianus.im.repository.MessageRepository
import tech.loucianus.im.service.MessageService

@Service
class MessageServiceImpl: MessageService {

    companion object {
        private val log = LogFactory.getLog(this::class.java)
    }

    @Autowired lateinit var messageRepository: MessageRepository

    override fun getHistoryMessage(id: Int, uid:Int, msg: String): Page<Message> {
        return messageRepository.findHistoryMessage(id, uid, msg)
    }

    override fun getGroupHistoryMessage(msg: String): Page<Message> {
        return messageRepository.findGroupHistoryMessage(msg)
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

    override fun actMessage(uid: Int, id: Int): Int {
        return messageRepository.actMessage(uid, id)
    }
}