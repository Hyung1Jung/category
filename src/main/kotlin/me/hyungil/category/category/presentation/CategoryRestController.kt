package me.hyungil.category.category.presentation

import me.hyungil.category.category.application.CategoryService
import me.hyungil.category.category.presentation.dto.request.CategoryCreateRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("api/v1/categories")
class CategoryRestController(
    private val categoryService: CategoryService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCategory(@RequestBody @Valid request: CategoryCreateRequest) = categoryService.createCategory(request)
}