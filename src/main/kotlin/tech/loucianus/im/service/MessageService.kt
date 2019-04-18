package tech.loucianus.im.service

import com.github.pagehelper.Page
import tech.loucianus.im.model.po.Message

interface MessageService {

    /**
     * Get the history message
     *
     * @param id The message getter's id.
     * @param uid The message sender's id.
     * @return The message list that size is lower than 10.
     */
    fun getHistoryMessage(id: Int, uid:Int): Page<Message>

    /**
     * If needed to get group history message, use this method.
     */
    fun getGroupHistoryMessage(): Page<Message>

    /**
     * Search message.
     *
     * @param id The message getter's id.
     * @param uid The message sender's id.
     * @param msg The key msg.
     * @return
     */
    fun searchMessage(id: Int, uid: Int, msg: String): Page<Message>

    /**
     * Save message to database
     *
     * @param message The chatting message.
     */
    fun setMessage(message: Message)
}