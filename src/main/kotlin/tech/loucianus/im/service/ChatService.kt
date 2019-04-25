package tech.loucianus.im.service

import tech.loucianus.im.model.vo.MessageGetter
import tech.loucianus.im.model.dao.GroupMessage
import tech.loucianus.im.model.po.Message

interface ChatService {
    /**
     * Send message.
     *
     * @param message the message that principal.name send.
     */
    fun sentToUser(message: MessageGetter)

    fun sentToGroup(message: MessageGetter): GroupMessage

    /**
     * Get the newest 10 messages.
     *
     * @param id the getter.
     * @param uid the targeter.
     * @return The message list.
     */
    fun getChatFile(id :Int, uid: Int): List<Message>

    /**
     * Get the newest 10 messages.
     * @return The group message list.
     */
    fun getGroupChatFile(): List<GroupMessage>
}