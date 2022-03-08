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

    override fun getCategories(parentCategory: Category): List<GetCategoryResponse> {
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
                ).and(category.isDeleted.eq(false))
            ).fetch()
    }

    override fun getAllCategories(): List<GetCategoryResponse> {
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
            .where(category.isDeleted.eq(false))
            .orderBy(
                category.hierarchy.rootCategory.id.asc(),
                category.hierarchy.leftNode.asc()
            )
            .fetch()
    }

    override fun findByIdWithRootCategory(id: Long?): Category? =
        jpaQueryFactory.selectFrom(category)
            .innerJoin(category.hierarchy.rootCategory)
            .fetchJoin()
            .where(category.id.eq(id).and(category.isDeleted.eq(false)))
            .fetchOne()

    override fun adjustHierarchyOrders(newCategory: Category) {
        jpaQueryFactory.update(category)
            .set(category.hierarchy.leftNode, category.hierarchy.leftNode.add(2))
            .where(
                category.hierarchy.leftNode.goe(newCategory.getRightNode())
                    .and(
                        category.hierarchy.rootCategory.eq(newCategory.getRootCategory())
                            .and(category.isDeleted.eq(false))
                    )
            )
            .execute()

        jpaQueryFactory.update(category)
            .set(category.hierarchy.rightNode, category.hierarchy.rightNode.add(2))
            .where(
                category.hierarchy.rightNode.goe(newCategory.getLeftNode())
                    .and(
                        category.hierarchy.rootCategory.eq(newCategory.getRootCategory())
                            .and(category.isDeleted.eq(false))
                    )
            )
            .execute()
    }

    override fun deleteCategory(parentCategory: Category) {
        jpaQueryFactory.update(category)
            .set(category.isDeleted, true)
            .where(
                category.id.eq(parentCategory.id).or(
                    category.hierarchy.leftNode.gt(parentCategory.getLeftNode())
                        .and(
                            category.hierarchy.rightNode.lt(parentCategory.getRightNode())
                                .and(category.hierarchy.rootCategory.eq(parentCategory.getRootCategory()))
                                .and(category.isDeleted.eq(false))
                        )
                )
            )
            .execute()
    }
    /*
    만약 카테고리의 위치도 변경하는 API를 만든다면 하위 카테고리들까지 모두 이동시킬 때
    1. rightNode - leftNode == 1이면 카테고리가 존재하지 않도록,
    2. rightNode - leftNode != 1이면 하위 카테고리리 존재하도록,
    3. 어떤 특정한 카테고리에 하위 카테고리를 삽입하면, 해당 하위 노드의 leftNode는 상위 카테고리의 rightNode가 되고 rightNode는 상위 카테고리의 rightNode + 1이 되도록

    하는 위 3가지의 조건을 만족하는 기능을 아주 간단한 로직 + 쿼리로 구현해보고 싶은 욕심이 있는데 고민이 조금 더 필요할 것 같습니다.

    override fun updateCategory(parentCategory: Category) {
        jpaQueryFactory.update(category)
            .set(category.hierarchy.leftNode, category.hierarchy.leftNode.add(parentCategory.getRightNode() + 1L))
            .set(category.hierarchy.rightNode, parentCategory.getRightNode() * 2)
            .set(category.hierarchy.rightNode, category.hierarchy.rightNode.add(-2))
            .set(category.hierarchy.depth, category.hierarchy.depth.add(1))
            .set(category.hierarchy.rootCategory, parentCategory.getRootCategory())
            .where(
                category.hierarchy.leftNode.gt(parentCategory.getLeftNode())
                    .and(
                        category.hierarchy.rightNode.lt(parentCategory.getRightNode())
                            .and(category.isDeleted.eq(false))
                    )
            )
            .execute()
    }
    */
}
