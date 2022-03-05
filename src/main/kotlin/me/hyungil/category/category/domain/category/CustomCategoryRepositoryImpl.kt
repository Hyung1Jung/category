package me.hyungil.category.category.domain.category

import com.querydsl.jpa.impl.JPAQueryFactory
import me.hyungil.category.category.domain.category.QCategory.category
import org.springframework.stereotype.Repository

@Repository
class CustomCategoryRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CustomCategoryRepository {

    override fun findByIdWithRootCategory(id: Long): Category? =
        jpaQueryFactory.selectFrom(category)
        .innerJoin(category.hierarchy.rootCategory)
        .fetchJoin()
        .where(category.id.eq(id))
        .fetchOne()
}
