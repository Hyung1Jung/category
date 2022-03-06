package me.hyungil.category.category.presentation.dto.response

import me.hyungil.category.category.domain.category.Category
import java.time.LocalDateTime

data class GetCategoryResponse(

    val categoryId: Long,

    val name: String,

    val depth: Int,

    val createDate: LocalDateTime,

    val parentCategory: Long?,

    val rootCategory: Long?
) {
    companion object {
        fun from(category: Category) = GetCategoryResponse(
            category.id,
            category.name,
            category.getDepth(),
            category.createdDate,
            category.getParentCategoryId(),
            category.getRootCategoryId()
        )
    }
}