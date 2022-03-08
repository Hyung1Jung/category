package me.hyungil.category.category.domain.hierachy

import me.hyungil.category.category.domain.category.Category
import javax.persistence.*

@Embeddable
class Hierarchy {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_category_id")
    private var rootCategory: Category? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private var parentCategory: Category? = null

    @Column(nullable = false)
    private var leftNode: Long = 1L

    @Column(nullable = false)
    private var rightNode: Long = 2L

    @Column(nullable = false)
    private var depth: Long = 1L

    fun createRootCategory(rootCategory: Category) {
        this.rootCategory = rootCategory
    }

    fun createSubCategory(parentCategory: Category, subCategoryHierarchy: Hierarchy) {
        subCategoryHierarchy.parentCategory = parentCategory
        subCategoryHierarchy.rootCategory = this.rootCategory
        subCategoryHierarchy.leftNode = this.rightNode
        subCategoryHierarchy.rightNode = this.rightNode + 1L
        subCategoryHierarchy.depth = this.depth + 1L
    }

    fun getRootCategory() = rootCategory
    fun getLeftNode() = leftNode
    fun getRightNode() = rightNode
}