package me.hyungil.category.acceptance

import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import me.hyungil.category.category.presentation.dto.request.CreateCategoryRequest
import me.hyungil.category.category.presentation.dto.request.UpdateCategoryRequest
import org.apache.http.HttpHeaders
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus


class CategoryAcceptanceTest : AcceptanceTest() {

    @Test
    @DisplayName("카테고리를 생성")
    fun createCategory() {
        val request = CreateCategoryRequest(0L, "상의")
        val response: ExtractableResponse<Response> = createCategory(request)
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }

    @Test
    @DisplayName("카테고리를 삭제한다.")
    fun deleteCategory() {
        val createRequest = CreateCategoryRequest(0L, "상의")
        val header = createCategory(createRequest).header(HttpHeaders.LOCATION)

        val statusCode = deleteCategory(id(header)).statusCode()
        assertThat(statusCode).isEqualTo(HttpStatus.NO_CONTENT.value())
    }

    @DisplayName("카테고리를 수정한다.")
    @Test
    fun updateCategory() {
        val createRequest = CreateCategoryRequest(0L, "상의")
        val header: String = createCategory(createRequest).header(HttpHeaders.LOCATION)
        val updateRequest = UpdateCategoryRequest( "장신구")
        val statusCode = updateCategory(id(header), updateRequest).statusCode()

        assertThat(statusCode).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    @DisplayName("카테고리를 조회한다.")
    fun getCategories() {
        val header = createCategory(CreateCategoryRequest(0L, "상의")).header(HttpHeaders.LOCATION)

        createCategory(CreateCategoryRequest(id(header), "상의")).header(HttpHeaders.LOCATION)

        val response: ExtractableResponse<Response> = getCategories(id(header))

        assertThat(response).isNotNull
    }

    private fun id(header: String): Long {
        val splitLocation = header.split("/").toTypedArray()
        return (splitLocation[splitLocation.size - 1]).toLong()
    }
}