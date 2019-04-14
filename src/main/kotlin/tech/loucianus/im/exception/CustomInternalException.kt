package tech.loucianus.im.exception

class CustomInternalException : AbstractCustomException {

    constructor(): super()

    constructor(message: String): super(message)
}