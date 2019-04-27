package tech.loucianus.im.service.impl

import org.apache.commons.logging.LogFactory
import org.jasypt.encryption.StringEncryptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import tech.loucianus.im.exception.CustomNotFoundException
import tech.loucianus.im.model.vo.Contacts
import tech.loucianus.im.model.dao.Permission
import tech.loucianus.im.model.dao.WorkerStorer
import tech.loucianus.im.model.dao.WorkerUpdater
import tech.loucianus.im.model.po.File
import tech.loucianus.im.model.po.Worker
import tech.loucianus.im.repository.WorkerRepository
import tech.loucianus.im.service.WorkerService
import tech.loucianus.im.util.FileUtil

@Service
@CacheConfig(cacheNames = ["user"])
class WorkerServiceImpl: WorkerService {

    companion object {
        private val log = LogFactory.getLog(this::class.java)
    }

    @Autowired lateinit var workerRepository: WorkerRepository
    @Autowired lateinit var cipher: StringEncryptor
    @Value("\${web.img.path}") lateinit var localPath:String

    override fun getWorker(email: String): Worker {
        return workerRepository.findWorkerByEmail(email)
    }

    @Cacheable(key = "#id")
    override fun getWorker(id: Int): Worker {
        if (log.isInfoEnabled) log.info("Get User")
        return workerRepository.findWorkerById(id)
    }

    override fun getPassword(email: String): String? {
        return workerRepository.findPasswordByEmail(email)?.let { password ->
            if (log.isInfoEnabled) log.info("get password:: password->$password")
            return cipher.decrypt(password)
        }
    }

    override fun setWorker(workerStorer: WorkerStorer): Boolean {
        return if (workerStorer.permission == "") {
            workerRepository.saveWorker(
                WorkerStorer(
                    email = workerStorer.email,
                    name = workerStorer.name,
                    role = workerStorer.role,
                    gender = workerStorer.gender)
            ) == 1
        } else {
            workerRepository.saveWorker(workerStorer) == 1
        }
    }

    override fun updateWorkerPermission(workerUpdater: WorkerUpdater): Boolean {
        if (getWorker(workerUpdater.id).permission == "view#edict#download#upload#update#delete")
            return false

        return if (workerUpdater.permission == "") {
            workerRepository.updateWorkerPermission(
                    WorkerUpdater(id = workerUpdater.id,
                            status = workerUpdater.status,
                            role = workerUpdater.role)
            ) == 1
        } else {
            workerRepository.updateWorkerPermission(workerUpdater) == 1
        }
    }

    override fun updateWorkerInfo(uid: Int, name: String, gender: String, portrait: MultipartFile?): Boolean {

        return if (portrait == null) {
            val imgPath = workerRepository.findWorkerById(uid).portrait
            workerRepository.updateWorkerInfo(uid, name, gender, imgPath) == 1
        } else {
            if (portrait.originalFilename == null) {
                throw CustomNotFoundException("File not exits!!")
            }

            val filename = FileUtil.getNewFileName(portrait.originalFilename!!)
            val suffix = FileUtil.getSuffix(filename)
            return if (FileUtil.upload(portrait, localPath, "$uid.$suffix")) {
                workerRepository.updateWorkerInfo(uid, name, gender, "images/$filename") == 1
            } else {
                false
            }
        }
    }
    
    override fun verify(username: String, password: String): Boolean {
        getPassword(username)?.let {
            if (log.isInfoEnabled) log.info("verify:: it->$it; password->$password")
            return password == it
        }
        return false
    }

    override fun getContacts(email: String): List<Contacts> {
        val contacts = workerRepository.findContactsByEmail(email) as MutableList
        contacts.add(
            index = 0,
            element = Contacts(
                id = 0,
                name = "Group Message.",
                portrait = "static/img/portrait/group.jpg",
                email = "citrine@loucianus.tech",
                role = "Group Message."
            )
        )

        return contacts
    }

    @Cacheable(cacheNames = ["authority"], key = "#email")
    override fun getAuthority(email: String): Permission {
        return workerRepository.getAuthority(email)
    }
}