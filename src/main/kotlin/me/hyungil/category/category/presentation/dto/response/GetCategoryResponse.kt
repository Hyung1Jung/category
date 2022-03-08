package me.hyungil.category.category.presentation.dto.response

import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

data class GetCategoryResponse(

    @ApiModelProperty(value = "카테고리 ID", required = true, example = "2")
    val id: Long,

    @ApiModelProperty(value = "카테고리 이름", required = true, example = "상의")
    val name: String,

    @ApiModelProperty(value = "카테고리의 계층 높이", required = true, example = "2")
    val depth: Long,

    @ApiModelProperty(value = "카테고리 생성 날짜", required = true, example = "2022-03-07T18:32:28")
    val createDate: LocalDateTime,

    @ApiModelProperty(value = "부모 카테고라", example = "1")
    val parentCategory: Long?,

    @ApiModelProperty(value = "루트 카테고라", required = true, example = "1")
    val rootCategory: Long
)
