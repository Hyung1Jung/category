package me.hyungil.category.category.domain.hierachy

import me.hyungil.category.category.domain.category.Category
import javax.persistence.*

@Embeddable
class Hierarchy {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_category_id")
    private lateinit var rootCategory: Category

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private lateinit var parentCategory: Category

    @Column(nullable = false)
    private var leftNode: Int = 1

    @Column(nullable = false)
    private var rightNode: Int = 2

    @Column(nullable = false)
    private var depth: Int = 1

    fun updateUpdateSubCategory(subCategory: Category) {
        subCategory.hierarchy.rootCategory = this.rootCategory
        subCategory.hierarchy.depth += 1
        subCategory.hierarchy.leftNode = this.rightNode
        subCategory.hierarchy.rightNode += 1
    }

    fun updateRootCategory(rootCategory: Category) {
        this.rootCategory = rootCategory
    }

    fun updateParentCategory(parentCategory: Category) {
        this.parentCategory = parentCategory
    }

    fun getDepth() = depth
}