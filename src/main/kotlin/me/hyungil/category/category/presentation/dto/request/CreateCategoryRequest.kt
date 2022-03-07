package me.hyungil.category.category.presentation.dto.request

import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank

data class CreateCategoryRequest(

    @ApiModelProperty(value = "부모 카테고리 ID", example = "1")
    val parentCategoryId: Long?,

    @ApiModelProperty(value = "카테고리 이름", required = true, example = "반팔티셔츠")
    @field:NotBlank(message = "잘못된 정보를 입력하셨습니다.")
    val name: String
)