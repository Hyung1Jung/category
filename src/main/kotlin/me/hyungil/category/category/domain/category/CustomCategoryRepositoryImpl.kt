package me.hyungil.category.category.domain.category

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import me.hyungil.category.category.domain.category.QCategory.category
import me.hyungil.category.category.presentation.dto.response.GetCategoryResponse
import org.springframework.stereotype.Repository

@Repository
class CustomCategoryRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CustomCategoryRepository {

    override fun findByIdWithRootCategory(id: Long?): Category? =
        jpaQueryFactory.selectFrom(category)
            .innerJoin(category.hierarchy.rootCategory)
            .fetchJoin()
            .where(category.id.eq(id))
            .fetchOne()

    override fun adjustHierarchyOrders(newCategory: Category) {
        jpaQueryFactory.update(category)
            .set(category.hierarchy.leftNode, category.hierarchy.leftNode.add(2))
            .where(
                category.hierarchy.leftNode.goe(newCategory.getRightNode())
                    .and(category.hierarchy.rootCategory.eq(newCategory.getRootCategory()))
            )
            .execute()

        jpaQueryFactory.update(category)
            .set(category.hierarchy.rightNode, category.hierarchy.rightNode.add(2))
            .where(
                category.hierarchy.rightNode.goe(newCategory.getLeftNode())
                    .and(category.hierarchy.rootCategory.eq(newCategory.getRootCategory()))
            )
            .execute()
    }

    override fun deleteCategory(parentCategory: Category) {
        jpaQueryFactory.delete(category)
            .where(
                category.id.eq(parentCategory.id).or(
                    category.hierarchy.leftNode.gt(parentCategory.getLeftNode())
                        .and(
                            category.hierarchy.rightNode.lt(parentCategory.getRightNode())
                                .and(category.hierarchy.rootCategory.eq(parentCategory.getRootCategory()))
                        )
                )
            )
            .execute()
    }

    override fun getCategories(parentCategory: Category): List<GetCategoryResponse>? {
        return jpaQueryFactory.select(
            Projections.constructor(
                GetCategoryResponse::class.java,
                category.id,
                category.name,
                category.hierarchy.depth,
                category.createdDate,
                category.hierarchy.parentCategory.id,
                category.hierarchy.rootCategory.id
            )
        )
            .from(category)
            .where(
                category.id.eq(parentCategory.id).or(
                    category.hierarchy.leftNode.gt(parentCategory.getLeftNode())
                        .and(
                            category.hierarchy.rightNode.lt(parentCategory.getRightNode())
                                .and(category.hierarchy.rootCategory.eq(parentCategory.getRootCategory()))
                        )
                )
            ).fetch()
    }
}
