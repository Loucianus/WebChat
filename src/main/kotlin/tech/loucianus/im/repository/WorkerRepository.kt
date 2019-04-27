package tech.loucianus.im.repository

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update
import org.springframework.stereotype.Repository
import tech.loucianus.im.model.vo.Contacts
import tech.loucianus.im.model.dao.Permission
import tech.loucianus.im.model.dao.WorkerStorer
import tech.loucianus.im.model.dao.WorkerUpdater
import tech.loucianus.im.model.po.Worker

@Repository
interface WorkerRepository {

    @Select("select * " +
            "from worker " +
            "where email=#{email}")
    fun findWorkerByEmail(@Param("email") email: String): Worker

    @Select("select * " +
            "from worker " +
            "where id=#{id} ")
    fun findWorkerById(@Param("id") id: Int): Worker

    @Select("select password " +
            "from worker " +
            "where email=#{email}")
    fun findPasswordByEmail(@Param("email") email: String): String?

    @Select("select w.id, w.name, w.portrait , w.email, w.role " +
            "from worker w " +
            "where w.email!=#{email} " +
            "order by w.name " +
            "asc")
    fun findContactsByEmail(@Param("email") email: String): List<Contacts>

    @Select("select role, permission " +
            "from worker " +
            "where email=#{email}")
    fun getAuthority(@Param("email") email: String): Permission

    @Insert("insert into worker (email, name, role, permission, gender) " +
            "values (#{email}, #{name}, #{role}, #{permission}, #{gender})")
    fun saveWorker(workerStorer: WorkerStorer): Int

    @Update("update worker set role=#{role}, permission=#{permission}, status=#{status} " +
            "where id=#{id}")
    fun updateWorkerPermission(workerUpdater: WorkerUpdater): Int

    @Update("update worker set name=#{name}, gender=#{gender}, portrait=#{portrait} where id=#{uid}")
    fun updateWorkerInfo(@Param("uid")uid: Int,
                         @Param("name") name: String,
                         @Param("gender") gender: String,
                         @Param("portrait") portrait: String): Int
}