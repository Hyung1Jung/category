package me.hyungil.category.acceptance

import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import me.hyungil.category.category.presentation.dto.request.CreateCategoryRequest
import me.hyungil.category.category.presentation.dto.request.UpdateCategoryRequest
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AcceptanceTest {

    @LocalServerPort
    val port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
    }

    protected fun createCategory(createCategoryRequest: CreateCategoryRequest?): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(createCategoryRequest)
            .`when`()
            .post("/api/v1/categories")
            .then()
            .extract()
    }

    protected fun deleteCategory(id: Long): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .`when`()
            .delete("/api/v1/categories/$id")
            .then()
            .extract()
    }

    protected fun updateCategory(id: Long, updateCategoryRequest: UpdateCategoryRequest): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(updateCategoryRequest)
            .`when`()
            .put("/api/v1/categories/$id")
            .then()
            .extract()
    }

    protected fun getCategories(id: Long): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .`when`()
            .get("/api/v1/categories/$id")
            .then()
            .extract()
    }
}