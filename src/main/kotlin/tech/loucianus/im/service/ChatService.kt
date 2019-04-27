package tech.loucianus.im.service

import tech.loucianus.im.model.vo.MessageGetter
import tech.loucianus.im.model.dao.GroupMessage
import tech.loucianus.im.model.po.Message

interface ChatService {

    fun sentToUser(message: MessageGetter)

    fun sentToGroup(message: MessageGetter): GroupMessage

}