package me.hyungil.category.category.application

import me.hyungil.category.category.commom.exception.CategoryNotFoundException
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
        val subCategory = findById(id)
        subCategory.updateCategoryName(request.name)
    }


    @Transactional
    @CacheEvict(allEntries = true)
    fun deleteCategory(id: Long) {
        val category = findById(id)
        categoryRepository.deleteCategory(category)
    }

    private fun createRootCategory(request: CreateCategoryRequest): Long {
        val createCategory = Category(request.name)
        val newCategory = categoryRepository.save(createCategory).apply { createRootCategory(this) }

        return newCategory.id
    }

    private fun createSubCategory(request: CreateCategoryRequest): Long {
        val parentCategory = findByIdWithRootCategory(request.parentCategoryId)
        val createSubCategory = Category(request.name)

        parentCategory.createSubCategory(createSubCategory)
        categoryRepository.adjustHierarchyOrders(createSubCategory)

        val newCategory = categoryRepository.save(createSubCategory)

        return newCategory.id
    }

    private fun findById(id: Long?): Category =
        categoryRepository.findByIdOrNull(id) ?: throw CategoryNotFoundException()

    private fun findByIdWithRootCategory(id: Long?) =
        categoryRepository.findByIdWithRootCategory(id) ?: throw CategoryNotFoundException()
}
