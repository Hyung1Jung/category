package me.hyungil.category.category.domain.category

import me.hyungil.category.category.presentation.dto.response.GetCategoryResponse

interface CustomCategoryRepository {

    fun getCategories(parentCategory: Category) : List<GetCategoryResponse>

    fun getAllCategories(): List<GetCategoryResponse>

    fun findByIdWithRootCategory(id: Long?): Category?

    fun deleteCategory(parentCategory: Category)

    fun adjustHierarchyOrders(newCategory: Category)
}