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

    @Select("<script>" +
            "select * from worker where " +
            "<foreach index='index' collection='list' item='id' separator=','>" +
            "id=#{id} " +
            "</foreach>" +
            "</script>")
    fun findWorkersById(ids : List<Int>): List<Worker>

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
    fun findContactsById(@Param("uid") id: Int): List<Contacts>

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

    @Insert("<script>" +
            "insert into worker (email, name, role, permission) " +
            "values " +
            "<foreach index='index' collection='list' item='worker' separator=','>" +
            "(#{worker.email}, #{worker.name}, #{worker.role}, #{worker.permission}) " +
            "</foreach>" +
            "</script>")
    fun saveWorkers(workerStorerList: List<WorkerStorer>): Int

    @Update("update worker set role=#{role}, permission=#{permission}, status=#{status} " +
            "where id=#{id}")
    fun updateWorkerByAdmin(workerUpdater: WorkerUpdater): Int
}