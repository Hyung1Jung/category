package me.hyungil.category.category.commom.enumeration

enum class ExceptionType(
    val message: String
) {
    NOT_FOUND_PARENT_CATEGORY("카테고리가 존재하지 않습니다."),
    INTERNAL_SERVER_ERROR_EXCEPTION("일시적으로 작업이 중단 되었습니다."),
}