package me.hyungil.category

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@EnableCaching
@SpringBootApplication
class CategoryApplication

fun main(args: Array<String>) {
    runApplication<CategoryApplication>(*args)
}
