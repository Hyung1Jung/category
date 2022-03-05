package me.hyungil.category.category.domain.category

interface CustomCategoryRepository {

    fun findByIdWithRootCategory(id: Long): Category?

    fun adjustHierarchyOrders(newCategory: Category)

    fun deleteChildCategories(parentCategory: Category)
}