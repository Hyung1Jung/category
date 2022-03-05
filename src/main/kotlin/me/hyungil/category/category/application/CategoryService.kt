package me.hyungil.category.category.application

import me.hyungil.category.category.commom.enumeration.ExceptionType.NOT_FOUND_PARENT_CATEGORY
import me.hyungil.category.category.commom.exception.PostNotFoundException
import me.hyungil.category.category.domain.category.Category
import me.hyungil.category.category.domain.category.CategoryRepository
import me.hyungil.category.category.presentation.dto.request.CategoryCreateRequest
import me.hyungil.category.category.presentation.dto.response.GetCategoryResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    @Transactional
    fun createCategory(request: CategoryCreateRequest) = when (request.parentCategoryId) {
        null, 0L -> createRootCategory(request)
        else -> createSubCategory(request)
    }

    private fun createSubCategory(request: CategoryCreateRequest): GetCategoryResponse {
        val parentCategory = findById(request.parentCategoryId)
        val rootCategory = findByIdWithRootCategory(parentCategory.id)

        val createSubCategory = Category(request.name).apply {
            updateRootCategory(rootCategory)
            updateParentCategory(parentCategory)
        }

        parentCategory.updateSubCategory(createSubCategory)

        return GetCategoryResponse.from(categoryRepository.save(createSubCategory))
    }

    private fun createRootCategory(request: CategoryCreateRequest): GetCategoryResponse {
        val createRootCategory = Category(request.name)

        return GetCategoryResponse.from(categoryRepository.save(createRootCategory).apply { updateRootCategory(this) })
    }

    private fun findById(id: Long?): Category =
        categoryRepository.findByIdOrNull(id) ?: throw PostNotFoundException(NOT_FOUND_PARENT_CATEGORY.message)

    private fun findByIdWithRootCategory(id: Long) =
        categoryRepository.findByIdWithRootCategory(id)
            ?: throw PostNotFoundException(NOT_FOUND_PARENT_CATEGORY.message)
}