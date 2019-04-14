package tech.loucianus.im.repository

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Repository
import tech.loucianus.im.model.entity.Bulletin

@Repository
interface BulletinRepository {

    @Select("select * from bulletin order by date desc limit 1")
    fun findCurrentBulletin(): Bulletin

    @Insert("insert into bulletin (id, title, date, path, publish_worker)" +
            "values(#{id}, #{title}, #{date}, #{path}, #{publishWorker})")
    fun saveBulletin(bulletin: Bulletin): Int

}