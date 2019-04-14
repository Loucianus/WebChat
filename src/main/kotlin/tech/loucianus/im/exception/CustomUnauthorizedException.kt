package tech.loucianus.im.exception

class CustomUnauthorizedException : AbstractCustomException {

    constructor(): super()

    constructor(message: String): super(message)
}