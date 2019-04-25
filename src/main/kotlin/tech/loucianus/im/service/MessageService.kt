package tech.loucianus.im.service

import tech.loucianus.im.model.po.Message

interface MessageService {

    /**
     * Get the history message
     *
     * @param id The message getter's id.
     * @param uid The message sender's id.
     * @return The message list that size is lower than 10.
     */
    fun getHistoryMessage(id: Int, uid:Int): List<Message>

    /**
     * If needed to get group history message, use this method.
     *
     * @return  The message list that size is lower than 10.
     */
    fun getGroupHistoryMessage(): List<Message>

}