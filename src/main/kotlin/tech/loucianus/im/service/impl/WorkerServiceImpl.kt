package tech.loucianus.im.service.impl

import org.apache.commons.logging.LogFactory
import org.jasypt.encryption.StringEncryptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import tech.loucianus.im.exception.CustomInternalException
import tech.loucianus.im.exception.CustomNotFoundException
import tech.loucianus.im.model.vo.Contacts
import tech.loucianus.im.model.dao.Permission
import tech.loucianus.im.model.dao.UpdatePwd
import tech.loucianus.im.model.dao.WorkerStorer
import tech.loucianus.im.model.dao.WorkerUpdater
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

    override fun updatePwd(updatePwd: UpdatePwd): Boolean {
        val idCard = workerRepository.findIdCardByEmail(updatePwd.email)
        idCard?.let {

            if (it == updatePwd.idCard) {
                return workerRepository.updatePwd(
                        cipher.encrypt(updatePwd.password),
                        updatePwd.email
                ) == 1
            } else{
                throw CustomNotFoundException("请确认身份证号与邮箱是否填写正确!")
            }
        }
        throw CustomNotFoundException("请确认身份证号与邮箱是否填写正确!")
    }

    override fun setWorker(workerStorer: WorkerStorer): Boolean {
        return if (workerStorer.permission == "") {
            workerRepository.saveWorker(
                WorkerStorer(
                        email = workerStorer.email,
                        name = workerStorer.name,
                        role = workerStorer.role,
                        gender = workerStorer.gender,
                        idCard = workerStorer.idCard)
            ) == 1
        } else {
            workerRepository.saveWorker(workerStorer) == 1
        }
    }

    override fun updateWorkerPermission(workerUpdater: WorkerUpdater): Boolean {
        if (getWorker(workerUpdater.id).permission == "view#edict#download#upload#update#delete")
            return false
        val worker = workerRepository.findWorkerById(workerUpdater.id)

        return if (workerUpdater.permission == "") {

            updatePermission(
                    worker.email,
                    WorkerUpdater(id = workerUpdater.id,
                    status = workerUpdater.status,
                    role = workerUpdater.role))
            true
        } else {
            updatePermission(worker.email, workerUpdater)
            true
        }
    }

    override fun updateWorkerInfo(uid: Int, name: String, gender: String, portrait: MultipartFile?): Boolean {

        return if (portrait == null) {
            val imgPath = workerRepository.findWorkerById(uid).portrait
            updateWorker(uid, name, gender, imgPath)
            true
        } else {
            if (portrait.originalFilename == null) {
                throw CustomNotFoundException("File not exits!!")
            }

            val filename = FileUtil.getNewFileName(portrait.originalFilename!!)
            val suffix = FileUtil.getSuffix(filename)
            if (suffix != "png" || suffix != "jpg" || suffix  != "jpeg") {
                 throw CustomInternalException("目前只支持png,jpg与jpeg格式的图片!!")
            }
            return if (FileUtil.upload(portrait, localPath, "$uid.$suffix")) {
                updateWorker(uid, name, gender, "images/$filename")
                true
            } else {
                false
            }
        }
    }

    @CachePut(key = "#uid")
    override fun updateWorker(uid: Int, name: String, gender: String, portrait: String): Worker {
        val result = workerRepository.updateWorkerInfo(uid, name, gender, portrait)
        return if (result == 1) {
            workerRepository.findWorkerById(uid)
        } else {
            throw CustomInternalException("缓存用户信息失败")
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

    @CachePut(cacheNames = ["authority"], key = "#email")
    override fun updatePermission(email: String, workerUpdater: WorkerUpdater): Permission {
        val result = workerRepository.updateWorkerPermission(
                WorkerUpdater(id = workerUpdater.id,
                        status = workerUpdater.status,
                        role = workerUpdater.role)
        )

        return if (result == 1) {
            workerRepository.getAuthority(email)
        } else {
            throw CustomInternalException("缓存用户权限失败")
        }
    }


}