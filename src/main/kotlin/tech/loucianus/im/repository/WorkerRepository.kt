package tech.loucianus.im.repository

import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Repository
import tech.loucianus.im.model.dto.Contact
import tech.loucianus.im.model.dto.Permission
import tech.loucianus.im.model.entity.Worker

@Repository
interface WorkerRepository {

    @Select("select * " +
            "from worker " +
            "where email=#{email} ")
    fun findWorkerByEmail(@Param("email") email: String): Worker

    @Select("select * " +
            "from worker " +
            "where id=#{id} ")
    fun findWorkerById(@Param("id") id: Int): Worker

    @Select("select password " +
            "from worker " +
            "where email=#{email}")
    fun findPasswordByEmail(@Param("email") email: String): String?

    @Select("select id " +
            "from worker " +
            "where email=#{email}")
    fun findIdByEmail(@Param("email") email: String): Int

    @Select("select w.id, w.name, w.portrait , w.email, w.role " +
            "from worker w " +
            "where w.id!=#{uid} " +
            "order by w.name " +
            "asc")
    fun findContactsById(@Param("uid") id: Int): List<Contact>

    @Select("select w.id, w.name, w.portrait , w.email, w.role " +
            "from worker w " +
            "where w.email!=#{email} " +
            "order by w.name " +
            "asc")
    fun findContactsByEmail(@Param("email") email: String): List<Contact>

    @Select("select id, role, permission " +
            "from worker " +
            "where email=#{email}")
    fun getAuthority(@Param("email") email: String): Permission
}