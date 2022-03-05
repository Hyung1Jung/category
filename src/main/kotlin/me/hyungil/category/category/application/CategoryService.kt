package me.hyungil.category.category.application

import me.hyungil.category.category.commom.enumeration.ExceptionType.NOT_FOUND_PARENT_CATEGORY
import me.hyungil.category.category.commom.exception.PostNotFoundException
import me.hyungil.category.category.domain.category.Category
import me.hyungil.category.category.domain.category.CategoryRepository
import me.hyungil.category.category.presentation.dto.request.CreateCategoryRequest
import me.hyungil.category.category.presentation.dto.request.UpdateCategoryRequest
import me.hyungil.category.category.presentation.dto.response.GetCategoryResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    @Transactional
    fun createCategory(request: CreateCategoryRequest) = when (request.parentCategoryId) {
        null, 0L -> createRootCategory(request)
        else -> createSubCategory(request)
    }

    @Transactional
    fun updateCategory(id: Long, request: UpdateCategoryRequest): GetCategoryResponse {
        val parentCategory = findByIdWithRootCategory(request.parentCategoryId)

        val subCategory = findById(id)

        parentCategory.updateSubCategory(request.name, subCategory)
        categoryRepository.adjustHierarchyOrders(subCategory)

        return GetCategoryResponse.from(subCategory)
    }

    @Transactional
    fun deleteCategory(id: Long) {
        val category = findById(id)
        return categoryRepository.deleteChildCategories(category)
    }

    private fun createRootCategory(request: CreateCategoryRequest): GetCategoryResponse {
        val createCategory = Category(request.name)

        return GetCategoryResponse.from(categoryRepository.save(createCategory).apply { createRootCategory(this) })
    }

    private fun createSubCategory(request: CreateCategoryRequest): GetCategoryResponse {
        val parentCategory = findByIdWithRootCategory(request.parentCategoryId)
        val createSubCategory = Category(request.name)

        parentCategory.createSubCategory(createSubCategory)
        categoryRepository.adjustHierarchyOrders(createSubCategory)

        return GetCategoryResponse.from(categoryRepository.save(createSubCategory))
    }

    private fun findById(id: Long?): Category =
        categoryRepository.findByIdOrNull(id) ?: throw PostNotFoundException(NOT_FOUND_PARENT_CATEGORY.message)

    private fun findByIdWithRootCategory(id: Long?) =
        categoryRepository.findByIdWithRootCategory(id)
            ?: throw PostNotFoundException(NOT_FOUND_PARENT_CATEGORY.message)
}
