package tech.loucianus.im.repository

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update
import org.springframework.stereotype.Repository
import tech.loucianus.im.model.po.Task

@Repository
interface TaskRepository {

    @Select("select * from task where progress != 'p'")
    fun findTasks(): List<Task>

    @Select("select * from task where id=#{id}")
    fun findTaskById(@Param("id") id: Int): Task

    @Select("select * from task where target_workers like CONCAT('%',#{uid},'%')")
    fun findTaskByByWorkerId(@Param("uid")uid: Int): List<Task>

    @Select("select sub_title from task where title=#{title}")
    fun findTaskSubTitleByTitle(@Param("title")title: String): List<String>

    @Update("update task set progress=#{progress} " +
            "where sub_title=#{subTitle} and target_workers like CONCAT('%',#{uid},'%')")
    fun updateTaskById(
        @Param("uid")uid: Int,
        @Param("subTitle")subTitle: String ,
        @Param("progress")progress: String): Int

    @Insert("<script>" +
            "insert into task (title, sub_title, progress, target_workers, publish_worker)" +
            "values" +
            "<foreach index='index' collection='list' item='task' separator=','>" +
            "(#{task.title}, #{task.subTitle}, #{task.progress}, #{task.targetWorkers}, #{task.publishWorker})" +
            "</foreach>" +
            "</script>")
    fun saveTask(taskList: List<Task>): Int
}