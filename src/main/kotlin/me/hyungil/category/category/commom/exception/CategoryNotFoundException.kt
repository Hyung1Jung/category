package me.hyungil.category.category.commom.exception

import me.hyungil.category.category.commom.enumeration.ExceptionType.NOT_FOUND_PARENT_CATEGORY

class CategoryNotFoundException(message: String) : RuntimeException(message) {
    constructor() : this(NOT_FOUND_PARENT_CATEGORY.message)
}