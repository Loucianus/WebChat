package tech.loucianus.im.service

import com.github.pagehelper.Page
import tech.loucianus.im.model.dao.GroupMessage
import tech.loucianus.im.model.po.Message

interface MessageService {

    fun getHistoryMessage(id: Int, uid:Int, msg: String): Page<Message>

    fun getGroupHistoryMessage(msg: String): Page<Message>

    fun getRecentFile(id :Int, uid: Int): List<Message>

    fun getGroupRecentFile(): List<GroupMessage>

    fun actMessage(uid: Int, id: Int): Int
}