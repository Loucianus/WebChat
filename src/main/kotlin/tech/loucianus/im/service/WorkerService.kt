package tech.loucianus.im.service

import tech.loucianus.im.model.dto.Account
import tech.loucianus.im.model.dto.Contact
import tech.loucianus.im.model.dto.Contacts
import tech.loucianus.im.model.dto.Permission
import tech.loucianus.im.model.entity.Worker

interface WorkerService {

    /**
     * Get user entity.
     *
     * @param email Email of user entity
     * @return If get the user entity with the "$username", return it; otherwise null.
     */
    fun getWorker(email: String): Worker

    /**
     * Get user entity.
     *
     * @param id id of user entity
     * @return If get the user entity with the "$username", return it; otherwise null.
     */
    fun getWorker(id: Int): Worker

    /**
     * Get password.
     *
     * @param email Username of user entity
     * @return If get the password with the "$username", return it; otherwise null.
     */
    fun getPassword(email: String): String?

    /**
     * @param email
     */
    fun getId(email: String): Int
    /**
     * Verify the password.
     *
     * @param account Account<username: String, password: String>
     * @return If password of account got equals the password from database, return true; otherwise false.
     */
    fun verify(account: Account): Boolean

    /**
     * Verify the password
     *
     * @param username String username
     * @param password String password
     * @return If password got equals the password from database, return true; otherwise false.
     */
    fun verify(username: String, password: String): Boolean

    /**
     * @param uid
     */
    fun getContacts(uid: Int): List<Contact>

    /**
     * @param email
     */
    fun getContacts(email: String): List<Contact>

    /**
     * @param uid
     */
    fun getCurrentMessageAndContactList(uid: Int): List<Contacts>

    /**
     * @param email
     */
    fun getCurrentMessageAndContactList(email: String): List<Contacts>

    /**
     * @param
     */
    fun getAuthority(email: String): Permission


}