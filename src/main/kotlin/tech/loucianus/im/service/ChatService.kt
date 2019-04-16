package tech.loucianus.im.service

import tech.loucianus.im.model.dto.MessageGetter
import tech.loucianus.im.model.dto.GroupMessage
import tech.loucianus.im.model.entity.Message

interface ChatService {
    /**
     * Send message.
     *
     * @param message the message that principal.name send.
     */
    fun sentToUser(message: MessageGetter)

    fun sentToGroup(message: MessageGetter): GroupMessage

    /**
     * Get all chat content file.
     *
     * @param id the getter.
     * @param uid the targeter.
     * @return the all message of chat.
     */
    fun getChatFile(id :Int, uid: Int): List<Message>

    fun getGroupChatFile(): List<GroupMessage>
}