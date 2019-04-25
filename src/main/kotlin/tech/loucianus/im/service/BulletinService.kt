package tech.loucianus.im.service

import tech.loucianus.im.model.vo.BulletinDetails

interface BulletinService {

    /**
     * Get Bulletin.
     *
     * From the database to get the newest bulletin.
     *
     * @return [BulletinDetails].
     */
    fun getBulletin(): BulletinDetails

    /**
     * Set Bulletin.
     *
     * Get the details of bulletin to save to database.
     *
     * @param bulletinDetails See data class [BulletinDetails].
     * @return If succeed to save the bulletin, return true; otherwise false or throw exp.
     */
    fun setBulletin(bulletinDetails: BulletinDetails): Boolean
}