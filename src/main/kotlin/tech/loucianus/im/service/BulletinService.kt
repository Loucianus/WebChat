package tech.loucianus.im.service

import tech.loucianus.im.model.vo.BulletinDetails

interface BulletinService {

    /**
     * Get Bulletin
     *
     * From the database to get the newest bulletin.
     *
     * @return [BulletinDetails]
     */
    fun getBulletin(): BulletinDetails

    /**
     * Set Bulletin
     *
     *
     */
    fun setBulletin(bulletinDetails: BulletinDetails)
}