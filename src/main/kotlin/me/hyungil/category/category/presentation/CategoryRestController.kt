package me.hyungil.category.category.presentation

import io.swagger.annotations.ApiOperation
import me.hyungil.category.category.application.CategoryService
import me.hyungil.category.category.presentation.dto.request.CreateCategoryRequest
import me.hyungil.category.category.presentation.dto.request.UpdateCategoryRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

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
    fun createCategory(@RequestBody @Valid request: CreateCategoryRequest) = categoryService.createCategory(request)

    @PutMapping("/{id}")
    @ApiOperation(
        value = "특정 카테고리 수정",
        notes = """
            특정 카테고리의 위치 또는 카테고리명을 수정한다.
            1. parentCategoryId에 null 또는 0 입력시 해당 카테고리명만 수정
            2. parentCategoryId에 이동하고 싶은 부모 카테고리 ID 입력시, 입력한 부모 카테고리의 하위로 위치 변경
        """)
    fun updateCategory(@PathVariable id: Long, @RequestBody @Valid request: UpdateCategoryRequest) {
        categoryService.updateCategory(id, request)
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "특정 카테고리 삭제", notes = "특정 카테고리 삭제시, 하위 카테고리들도 함께 삭제")
    fun deleteCategory(@PathVariable id: Long) {
        categoryService.deleteCategory(id)
    }
}