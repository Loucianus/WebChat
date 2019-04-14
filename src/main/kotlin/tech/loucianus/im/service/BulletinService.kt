package tech.loucianus.im.service

import tech.loucianus.im.model.dto.BulletinDetails

interface BulletinService {

    fun getBulletin(): BulletinDetails

    fun setBulletin(bulletinDetails: BulletinDetails)
}