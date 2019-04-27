package tech.loucianus.im.service

import tech.loucianus.im.model.vo.BulletinDetails

interface BulletinService {

    fun getBulletin(): BulletinDetails

    fun setBulletin(bulletinDetails: BulletinDetails): Boolean
}