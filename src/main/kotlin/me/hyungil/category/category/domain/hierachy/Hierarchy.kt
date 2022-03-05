package me.hyungil.category.category.domain.hierachy

import me.hyungil.category.category.domain.category.Category
import javax.persistence.*

@Embeddable
class Hierarchy() {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_category_id")
    private var rootCategory: Category ?= null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private var parentCategory: Category ?= null

    @Column(nullable = false)
    private var leftNode: Int = 1

    @Column(nullable = false)
    private var rightNode: Int = 2

    @Column(nullable = false)
    private var depth: Int = 1

    fun createSubCategory(parentCategory: Category, subCategoryHierarchy: Hierarchy) {
        subCategoryHierarchy.parentCategory = parentCategory
        subCategoryHierarchy.rootCategory = this.rootCategory
        subCategoryHierarchy.leftNode = this.rightNode
        subCategoryHierarchy.rightNode += 1
        subCategoryHierarchy.depth += 1
    }

    fun createRootCategory(rootCategory: Category) {
        this.rootCategory = rootCategory
    }

    fun updateSubCategory(parentCategory: Category, subCategoryHierarchy: Hierarchy) {
        subCategoryHierarchy.leftNode = 1
        subCategoryHierarchy.rightNode = 2
        subCategoryHierarchy.depth = 1

        subCategoryHierarchy.parentCategory = parentCategory
        subCategoryHierarchy.rootCategory = this.rootCategory
        subCategoryHierarchy.leftNode = this.rightNode
        subCategoryHierarchy.rightNode += 1
        subCategoryHierarchy.depth += 1
    }

    fun getRootCategory() = rootCategory
    fun getDepth() = depth
    fun getLeftNode() = leftNode
    fun getRightNode() = rightNode
}