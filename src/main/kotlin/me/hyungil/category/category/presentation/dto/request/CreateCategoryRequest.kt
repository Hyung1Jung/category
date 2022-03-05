package me.hyungil.category.category.presentation.dto.request

import javax.validation.constraints.NotBlank

data class CreateCategoryRequest(

    val parentCategoryId: Long?,

    @field:NotBlank(message = "잘못된 정보를 입력하셨습니다.")
    val name: String
)