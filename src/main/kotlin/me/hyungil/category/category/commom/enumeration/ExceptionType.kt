package me.hyungil.category.category.commom.enumeration

enum class ExceptionType(
    val message: String
) {
    NOT_FOUND_PARENT_CATEGORY("부모 카테고리가 존재하지 않습니다."),
    NOT_FOUND_ROOT_CATEGORY("루트 카테고리가 존재하지 않습니다.")
}