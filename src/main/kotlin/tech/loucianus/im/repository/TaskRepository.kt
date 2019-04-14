package tech.loucianus.im.repository

import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Repository
import tech.loucianus.im.model.entity.Task

@Repository
interface TaskRepository {

    @Select("select * from task where mode=1")
    fun getTasks(): List<Task>

    @Select("select * from task where id=#{id} and mode=1")
    fun getTaskById(@Param("id") id: Int): Task

    @Select("select * from task where task=#{task} and mode=1")
    fun getTaskByTask(@Param("task") task: String): Task

}