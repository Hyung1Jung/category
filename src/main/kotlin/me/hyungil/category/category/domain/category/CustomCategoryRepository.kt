package me.hyungil.category.category.domain.category

import me.hyungil.category.category.presentation.dto.response.GetCategoryResponse

interface CustomCategoryRepository {

    fun findByIdWithRootCategory(id: Long?): Category?

    fun adjustHierarchyOrders(newCategory: Category)

    fun deleteCategory(parentCategory: Category)

    fun getCategories(parentCategory: Category) : List<GetCategoryResponse>?
}