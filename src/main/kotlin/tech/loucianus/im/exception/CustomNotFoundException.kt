package tech.loucianus.im.exception

class CustomNotFoundException : AbstractCustomException {

    constructor(): super()

    constructor(message: String): super(message)
}