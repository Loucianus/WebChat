package tech.loucianus.im.repository

import com.github.pagehelper.Page
import org.apache.ibatis.annotations.*
import org.apache.ibatis.mapping.StatementType
import org.springframework.stereotype.Repository
import tech.loucianus.im.model.dao.GroupMessage
import tech.loucianus.im.model.po.Message

@Repository
interface MessageRepository {

    @Select("select * from message where ((from_id =#{id} and to_id=#{uid}) or (from_id =#{uid} and to_id =#{id})) and is_read='f' order by date desc")
    fun findMessage(@Param("id") id: Int,@Param("uid") uid: Int): List<Message>

    @Select("select * from group_msg where is_read='f' order by date desc")
    fun findGroupMessage(): List<GroupMessage>

    @Select("select * from message where (from_id =#{id} and to_id=#{uid}) or (from_id =#{uid} and to_id =#{id})")
    fun findHistoryMessage(@Param("id") id: Int,@Param("uid") uid: Int): List<Message>

    @Select("select * from message where to_id=0")
    fun findGroupHistoryMessage(): List<Message>

    @Select("select * from message " +
            "where (" +
                "(from_id =#{id} and to_id=#{uid}) or " +
                "(from_id =#{uid} and to_id =#{id})" +
            ") " +
            "and content like CONCAT('%',#{msg},'%')")
    fun findMessageByMsg(@Param("id")id: Int, @Param("uid") uid: Int, @Param("msg")msg: String): Page<Message>

    @Insert("insert into message (id, from_id, to_id, content, type, date, target, filename) " +
            "values (#{id}, #{fromId}, #{toId}, #{content}, #{type}, #{date}, #{target}, #{filename})")
    fun saveMessage(message: Message): Int

    @Update("update message set is_read='t' where ((from_id =#{id} and to_id=#{uid}) or (from_id =#{uid} and to_id =#{id})) and is_read='f' ")
    fun actMessage(@Param("uid") uid: Int, @Param("id") id: Int): Int
}