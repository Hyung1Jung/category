package me.hyungil.category.category.application

import me.hyungil.category.category.commom.exception.CategoryNotFoundException
import me.hyungil.category.category.commom.exception.InternalServerErrorException
import me.hyungil.category.category.domain.category.Category
import me.hyungil.category.category.domain.category.CategoryRepository
import me.hyungil.category.category.presentation.dto.request.CreateCategoryRequest
import me.hyungil.category.category.presentation.dto.request.UpdateCategoryRequest
import me.hyungil.category.category.presentation.dto.response.GetCategoryResponse
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@CacheConfig(cacheNames = ["getCategories", "getAllCategories"])
class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    @Transactional(readOnly = true)
    @Cacheable(value = ["getCategories"])
    fun getCategories(id: Long): List<GetCategoryResponse>? {
        val parentCategory = findById(id)

        return categoryRepository.getCategories(parentCategory)
    }

    @Transactional(readOnly = true)
    @Cacheable(value = ["getAllCategories"])
    fun getAllCategories(): List<GetCategoryResponse> = categoryRepository.getAllCategories()

    @Transactional
    @CacheEvict(allEntries = true)
    fun createCategory(request: CreateCategoryRequest) = when (request.parentCategoryId) {
        null, 0L -> createRootCategory(request)
        else -> createSubCategory(request)
    }

    @Transactional
    @CacheEvict(allEntries = true)
    fun updateCategory(id: Long, request: UpdateCategoryRequest) {

        if (request.parentCategoryId == null || request.parentCategoryId == 0L) {
            val subCategory = findById(id)
            subCategory.updateCategoryName(request.name)
        } else {
            val parentCategory = findByIdWithRootCategory(request.parentCategoryId)
            val subCategory = findById(id)

            try {
                subCategory.updateCategoryName(request.name)
                parentCategory.updateSubCategory(subCategory)
                categoryRepository.adjustHierarchyOrders(subCategory)
            } catch (exception: Exception) {
                throw InternalServerErrorException()
            }
        }
    }

    @Transactional
    @CacheEvict(allEntries = true)
    fun deleteCategory(id: Long) {
        val category = findById(id)

        return categoryRepository.deleteCategory(category)
    }

    private fun createRootCategory(request: CreateCategoryRequest): GetCategoryResponse {
        val createCategory = Category(request.name)

        return GetCategoryResponse.from(categoryRepository.save(createCategory).apply { createRootCategory(this) })
    }

    private fun createSubCategory(request: CreateCategoryRequest): GetCategoryResponse {
        val parentCategory = findByIdWithRootCategory(request.parentCategoryId)

        try {
            val createSubCategory = Category(request.name)
            parentCategory.createSubCategory(createSubCategory)
            categoryRepository.adjustHierarchyOrders(createSubCategory)

            return GetCategoryResponse.from(categoryRepository.save(createSubCategory))
        } catch (exception: Exception) {
            throw InternalServerErrorException()
        }
    }

    private fun findById(id: Long?): Category =
        categoryRepository.findByIdOrNull(id) ?: throw CategoryNotFoundException()

    private fun findByIdWithRootCategory(id: Long?) =
        categoryRepository.findByIdWithRootCategory(id) ?: throw CategoryNotFoundException()
}
