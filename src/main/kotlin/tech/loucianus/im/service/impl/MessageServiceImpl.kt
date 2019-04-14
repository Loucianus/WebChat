package tech.loucianus.im.service.impl

import com.github.pagehelper.Page
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tech.loucianus.im.model.entity.Message
import tech.loucianus.im.repository.MessageRepository
import tech.loucianus.im.service.MessageService

@Service
class MessageServiceImpl: MessageService {

    companion object {
        private val log = LogFactory.getLog(this::class.java)
    }

    @Autowired lateinit var messageRepository: MessageRepository

    override fun getHistoryMessage(id: Int, uid:Int): Page<Message> {
        return messageRepository.findHistoryMessage(id, uid)
    }

    override fun searchMessage(id: Int, uid: Int, msg: String): Page<Message> {
        val messageList = messageRepository.findMessageByMsg(id, uid, msg)
        if (log.isInfoEnabled) log.info("messageList::$messageList")
        return messageList
    }

    override fun setMessage(message: Message) {
        messageRepository.saveMessage(message)
    }
}