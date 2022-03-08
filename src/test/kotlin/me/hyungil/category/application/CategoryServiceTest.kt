package me.hyungil.category.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import me.hyungil.category.category.application.CategoryService
import me.hyungil.category.category.commom.exception.CategoryNotFoundException
import me.hyungil.category.category.domain.category.Category
import me.hyungil.category.category.domain.category.CategoryRepository
import me.hyungil.category.category.presentation.dto.request.CreateCategoryRequest
import me.hyungil.category.category.presentation.dto.request.UpdateCategoryRequest
import org.springframework.data.repository.findByIdOrNull

internal class CategoryServiceTest : BehaviorSpec() {

    private val categoryRepository: CategoryRepository = mockk(relaxed = true)
    private val categoryService = CategoryService(categoryRepository)

    init {

        Given("상위 카테고리를 이용해, 해당 카테고리의 하위의 모든 카테고리를 조회할 때,") {
            val categoryId = 1L

            `when`("카테고리가 존재하면") {
                val category = Category("반팔티셔츠")
                every { categoryRepository.findByIdOrNull(categoryId) } answers { category }

                Then("카테고리 생성에 성공한다") {
                    every { categoryService.getCategories(categoryId) } shouldNotBe null
                    every { categoryRepository.getCategories(category) } shouldNotBe null
                }
            }

            `when`("카테고리가 존재하지 않는다면") {
                every { categoryRepository.findByIdOrNull(categoryId) } returns null

                val exception =
                    shouldThrow<CategoryNotFoundException> { categoryService.getCategories(categoryId) }

                Then("예외메시지를 출력하고 카테고리 생성에 실패한다,") {
                    exception.message shouldBe "카테고리가 존재하지 않습니다."
                }
            }
        }

        Given("상위 카테고리를 지정하지 않을 시") {

            `when`("전체 카테고리를 조회하는 것에") {

                Then("성공한다") {
                    every { categoryService.getAllCategories() } shouldNotBe null
                    every { categoryRepository.getAllCategories() } shouldNotBe null
                }
            }
        }

        Given("카테고리 수정시,") {
            val request = UpdateCategoryRequest("상의")

            `when`("카테고리가 존재하면") {
                val category = Category(request.name)
                every { categoryRepository.findByIdOrNull(1L) } answers { category }

                Then("카테고리 생성에 성공한다") {
                    every { categoryService.updateCategory(1L, request) } shouldNotBe null
                }
            }

            `when`("카테고리가 존재하지 않는다면") {
                every { categoryRepository.findByIdOrNull(1L) } returns null

                val exception =
                    shouldThrow<CategoryNotFoundException> { categoryService.updateCategory(1L, request) }

                Then("예외메시지를 출력하고 카테고리 수정에 실패한다,") {
                    exception.message shouldBe "카테고리가 존재하지 않습니다."
                }
            }
        }

        Given("카테고리 생성을 요청할 때") {
            val request = CreateCategoryRequest(1L, "상의")

            `when`("카테고리 생성 요청 정보에 올바른 값을 입력했다면") {
                val category = Category(request.name)

                Then("카테고리 생성에 성공한다") {
                    every { categoryService.createCategory(request) } shouldNotBe null
                    every { categoryRepository.save(category) } shouldNotBe null
                }
            }

            `when`("부모 카테고리가 존재하지 않는다면") {
                every { categoryRepository.findByIdWithRootCategory(request.parentCategoryId) } returns null

                val exception =
                    shouldThrow<CategoryNotFoundException> { categoryService.createCategory(request) }

                Then("예외메시지를 출력하고 카테고리 생성에 실패한다,") {
                    exception.message shouldBe "카테고리가 존재하지 않습니다."
                }
            }
        }

        Given("특정 카테고리 삭제시") {
            val categoryId = 1L

            `when`("카테고리가 존재하면") {
                val category = Category("반팔티셔츠")
                every { categoryRepository.findByIdOrNull(categoryId) } answers { category }

                Then("하위 카테고리들도 함께 삭제에 성공한다") {
                    every { categoryService.deleteCategory(categoryId) } shouldNotBe null
                    every { categoryRepository.deleteCategory(category) } shouldNotBe null
                }
            }

            `when`("카테고리가 존재하지 않는다면") {
                every { categoryRepository.findByIdOrNull(categoryId) } returns null

                val exception =
                    shouldThrow<CategoryNotFoundException> { categoryService.getCategories(categoryId) }

                Then("예외메시지를 출력하고 카테고리 생성에 실패한다,") {
                    exception.message shouldBe "카테고리가 존재하지 않습니다."
                }
            }
        }
    }
}
