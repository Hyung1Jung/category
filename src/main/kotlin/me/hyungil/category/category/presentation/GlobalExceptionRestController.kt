package me.hyungil.category.category.presentation

import me.hyungil.category.category.commom.exception.CategoryNotFoundException
import me.hyungil.category.category.commom.exception.InternalServerErrorException
import me.hyungil.category.category.presentation.dto.response.ErrorResponse
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionRestController {

    companion object {
        private val logger: Logger = LogManager.getLogger(this)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(exception: MethodArgumentNotValidException): ErrorResponse? {
        val message = exception.bindingResult.fieldError!!.defaultMessage
        return message?.let { ErrorResponse.of(it) }.also { logger.debug(it, exception) }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CategoryNotFoundException::class)
    fun handleCategoryNotFoundException(exception: CategoryNotFoundException) =
        exception.message?.let { ErrorResponse.of(it) }.also { logger.debug(it, exception) }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerErrorException::class)
    fun handleInternalServerErrorException(exception: InternalServerErrorException) =
        exception.message?.let { ErrorResponse.of(it) }.also { logger.debug(it, exception) }
}