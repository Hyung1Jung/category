package me.hyungil.category.presentation

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import me.hyungil.category.category.application.CategoryService
import me.hyungil.category.category.commom.exception.CategoryNotFoundException
import me.hyungil.category.category.presentation.CategoryRestController
import me.hyungil.category.category.presentation.dto.request.CreateCategoryRequest
import me.hyungil.category.category.presentation.dto.request.UpdateCategoryRequest
import me.hyungil.category.category.presentation.dto.response.GetCategoryResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.doThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter
import java.time.LocalDateTime

@WebMvcTest(CategoryRestController::class)
internal class CategoryRestControllerTest @Autowired constructor(
    private val objectMapper: ObjectMapper,
    private val webApplicationContext: WebApplicationContext,
    private var mockMvc: MockMvc,
) {
    @MockBean
    private lateinit var categoryService: CategoryService

    @BeforeEach
    fun setUp() {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
            .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .build()
    }

    @Test
    fun `카테고리 생성에 성공할 경우 Http Status Code 201(Created) 리턴`() {
        val request = CreateCategoryRequest(2L, "반팔티셔츠")

        given(categoryService.createCategory(request)).willReturn(2L)

        mockMvc.perform(
            post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request))
        )
            .andExpect(status().isCreated)
    }

    @Test
    fun `존재하지 않는 부모 카테고리로 인해 카테고리 생성에 실패할 경우 Http Status Code 404(Not Found) 리턴`() {
        val request = CreateCategoryRequest(2L, "반팔티셔츠")

        doThrow(CategoryNotFoundException("카테고리가 존재하지 않습니다.")).`when`(categoryService).createCategory(request)

        mockMvc.perform(
            post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `잘못된 정보 입력으로 인해 카테고리 생성에 실패할 경우 Http Status Code 404(Not Found) 리턴`() {
        val request = CreateCategoryRequest(2L, " ")

        mockMvc.perform(
            post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `카테고리 수정에 성공할 경우 Http Status Code 200(Ok) 리턴`() {
        val request = UpdateCategoryRequest( "상의")

        mockMvc.perform(
            put("/api/v1/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request))
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `존재하지 않는 카테고리 아이디로 인해 카테고리 수정에 실패할 경우 Http Status Code 404(Not Found) 리턴`() {
        val request = UpdateCategoryRequest( "상의")

        doThrow(CategoryNotFoundException("카테고리가 존재하지 않습니다.")).`when`(categoryService).updateCategory(1L, request)

        mockMvc.perform(
            put("/api/v1/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `카테고리 조회에 성공할 경우 Http Status Code 200(Ok) 리턴`() {
        val response = ArrayList<GetCategoryResponse>()
        response.add(GetCategoryResponse(1L, "상위", 2L, LocalDateTime.now(), 1L, 1L))

        given(categoryService.getCategories(1L)).willReturn(response)

        mockMvc.perform(
            get("/api/v1/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(response))
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `존재하지 않는 카테고리로 인해 카테고리 조회에 실패할 경우 Http Status Code 404(Not Found) 리턴`() {

        doThrow(CategoryNotFoundException("카테고리가 존재하지 않습니다.")).`when`(categoryService).getCategories(1L)

        mockMvc.perform(
            get("/api/v1/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `모든 카테고리 조회에 성공할 경우 Http Status Code 200(Ok) 리턴`() {
        val response = ArrayList<GetCategoryResponse>()
        response.add(GetCategoryResponse(1L, "상위", 2L, LocalDateTime.now(), 1L, 1L))

        mockMvc.perform(
            get("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(response))
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `카테고리 삭제에 성공할 경우 Http Status Code 200(Ok) 리턴`() {

        mockMvc.perform(
            delete("/api/v1/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNoContent)
    }

    @Test
    fun `존재하지 않는 카테고리로 인해 카테고리 삭제에 실패할 경우 Http Status Code 404(Not Found) 리턴`() {

        doThrow(CategoryNotFoundException("카테고리가 존재하지 않습니다.")).`when`(categoryService).deleteCategory(1L)

        mockMvc.perform(
            delete("/api/v1/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }

    @Throws(JsonProcessingException::class)
    private fun toJsonString(dto: Any) = objectMapper.writeValueAsString(dto)
}

