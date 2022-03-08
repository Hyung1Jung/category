package me.hyungil.category.category.presentation

import io.swagger.annotations.ApiOperation
import me.hyungil.category.category.application.CategoryService
import me.hyungil.category.category.presentation.dto.request.CreateCategoryRequest
import me.hyungil.category.category.presentation.dto.request.UpdateCategoryRequest
import me.hyungil.category.category.presentation.dto.response.GetCategoryResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/v1/categories")
class CategoryRestController(
    private val categoryService: CategoryService
) {
    @GetMapping("/{id}")
    @ApiOperation(value = "특정 카테고리 목록 조회", notes = "상위 카테고리를 이용해, 해당 카테고리의 하위의 모든 카테고리를 조회")
    fun getCategories(@PathVariable("id") id: Long) = categoryService.getCategories(id)

    @GetMapping
    @ApiOperation(value = "전체 카테고리 목록 조회", notes = "상위 카테고리를 지정하지 않을 시, 전체 카테고리를 리턴")
    fun getAllCategories() = categoryService.getAllCategories()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
        value = "카테고리 생성",
        notes = """
            카테고리를 생성하다.
            1. parentCategoryId에 null 또는 0 입력시 Root Category 생성
            2. parentCategoryId에 부모 카테고리 ID 입력시, 하위 카테고리 생성
            """
    )
    fun createCategory(@RequestBody @Valid request: CreateCategoryRequest): ResponseEntity<Void> {
        val id = categoryService.createCategory(request)

        return ResponseEntity.created(URI.create("/api/v1/categories/$id")).build()
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "카테고리 수정", notes = "카테고리 수정를 수정한다")
    fun updateCategory(@PathVariable id: Long, @RequestBody @Valid request: UpdateCategoryRequest) {
        categoryService.updateCategory(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "특정 카테고리 삭제", notes = "특정 카테고리 삭제시, 하위 카테고리들도 함께 삭제")
    fun deleteCategory(@PathVariable id: Long) {
        categoryService.deleteCategory(id)
    }
}