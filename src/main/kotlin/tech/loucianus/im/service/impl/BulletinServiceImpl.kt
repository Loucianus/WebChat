package tech.loucianus.im.service.impl

import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tech.loucianus.im.exception.CustomInternalException
import tech.loucianus.im.model.dto.BulletinDetails
import tech.loucianus.im.model.entity.Bulletin
import tech.loucianus.im.repository.BulletinRepository
import tech.loucianus.im.repository.WorkerRepository
import tech.loucianus.im.service.BulletinService
import tech.loucianus.im.util.FileUtil
import java.io.IOException
import java.sql.Date

@Service
class BulletinServiceImpl: BulletinService {

    companion object {
        private val log = LogFactory.getLog(this::class.java)
    }

    @Autowired lateinit var bulletinRepository: BulletinRepository

    @Autowired lateinit var workerRepository: WorkerRepository

    override fun getBulletin(): BulletinDetails {

        val bulletin = bulletinRepository.findCurrentBulletin()

        val details = getDetails(bulletin.date)

        return BulletinDetails(
            id = bulletin.id,
            title = bulletin.title,
            date = bulletin.date,
            details = details,
            publishWorker = workerRepository.findWorkerById(bulletin.publishWorker).name,
            publishWorkerId = bulletin.publishWorker
        )
    }

    override fun setBulletin(bulletinDetails: BulletinDetails) {
        val path = setDetails(bulletinDetails.details, bulletinDetails.date)
        val bulletin = Bulletin(
            title = bulletinDetails.title,
            date = bulletinDetails.date,
            path = path,
            publishWorker = bulletinDetails.publishWorkerId
        )

        val result: Int = bulletinRepository.saveBulletin(bulletin)

        if (result == 0) {
            if (log.isInfoEnabled) log.info("Bulletin has false to be saved.")
            throw CustomInternalException("Bulletin has false to be saved.")
        } else if (result > 1) {
            if (log.isInfoEnabled) log.info("Bulletin has to be saved more than one.")
            throw CustomInternalException("Bulletin has to be saved more than one.")
        }
    }

    @Throws(IOException::class)
    private fun getDetails(date: Date): String {
        val filePath = "target/bulletin/$date.md"
        return FileUtil.getFileContent(filePath)
    }

    @Throws(IOException::class)
    private fun setDetails(text: String, date: Date): String {
        val filePath = "target/bulletin/$date.md"
        FileUtil.writeFile(filePath, text)
        return filePath
    }

}