package me.hyungil.category.category.domain.category

import com.querydsl.jpa.impl.JPAQueryFactory
import me.hyungil.category.category.domain.category.QCategory.category
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

    override fun deleteChildCategories(parentCategory: Category) {
        jpaQueryFactory.update(category)
            .set(category.isDeleted, true)
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
}
