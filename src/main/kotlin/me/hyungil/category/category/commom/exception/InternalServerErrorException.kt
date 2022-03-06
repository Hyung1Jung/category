package me.hyungil.category.category.commom.exception

import me.hyungil.category.category.commom.enumeration.ExceptionType

class InternalServerErrorException(message: String) : Exception(message) {
    constructor() : this(ExceptionType.NOT_FOUND_PARENT_CATEGORY.message)
}