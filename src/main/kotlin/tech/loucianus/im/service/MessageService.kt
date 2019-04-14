package tech.loucianus.im.service

import com.github.pagehelper.Page
import tech.loucianus.im.model.entity.Message

interface MessageService {

    fun getHistoryMessage(id: Int, uid:Int): Page<Message>

    fun searchMessage(id: Int, uid: Int, msg: String): Page<Message>

    fun setMessage(message: Message)
}