package me.hyungil.category.category.commom.exception

import me.hyungil.category.category.commom.enumeration.ExceptionType.INTERNAL_SERVER_ERROR_EXCEPTION

class InternalServerErrorException(message: String) : Exception(message) {
    constructor() : this(INTERNAL_SERVER_ERROR_EXCEPTION.message)
}