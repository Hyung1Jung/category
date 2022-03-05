package me.hyungil.category.category.domain.category

import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Long>, CustomCategoryRepository