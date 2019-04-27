package tech.loucianus.im.service

import tech.loucianus.im.model.dao.GroupMessage
import tech.loucianus.im.model.po.Message

interface MessageService {

    fun getHistoryMessage(id: Int, uid:Int): List<Message>

    fun getGroupHistoryMessage(): List<Message>

    fun getChatFile(id :Int, uid: Int): List<Message>

    fun getGroupChatFile(): List<GroupMessage>

    fun actMessage(uid: Int, id: Int): Int
}