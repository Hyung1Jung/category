package me.hyungil.category.category.presentation.dto.response

data class ErrorResponse(val message: String) {

    companion object {
        fun of(message: String) = ErrorResponse(message)
    }
}