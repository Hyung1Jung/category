package me.hyungil.category.category.domain.category

interface CustomCategoryRepository {

    fun findByIdWithRootCategory(id: Long): Category?
}