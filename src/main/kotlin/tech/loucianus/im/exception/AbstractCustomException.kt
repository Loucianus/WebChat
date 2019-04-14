package tech.loucianus.im.exception

abstract class AbstractCustomException : RuntimeException {

    constructor(): super()

    constructor(message: String): super(message)

}