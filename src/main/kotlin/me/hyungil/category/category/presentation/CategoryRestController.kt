package me.hyungil.category.category.presentation

import me.hyungil.category.category.application.CategoryService
import me.hyungil.category.category.presentation.dto.request.CreateCategoryRequest
import me.hyungil.category.category.presentation.dto.request.UpdateCategoryRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/categories")
class CategoryRestController(
    private val categoryService: CategoryService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCategory(@RequestBody @Valid request: CreateCategoryRequest) = categoryService.createCategory(request)

    @GetMapping("/{id}")
    fun getCategories(@PathVariable("id") id: Long) = categoryService.getCategories(id)

    @PutMapping("/{id}")
    fun updateCategory(@PathVariable id: Long, @RequestBody @Valid request: UpdateCategoryRequest) {
        categoryService.updateCategory(id, request)
    }

    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: Long) = categoryService.deleteCategory(id)
}