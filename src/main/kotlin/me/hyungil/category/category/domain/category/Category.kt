package me.hyungil.category.category.domain.category

import me.hyungil.category.category.domain.BaseTimeEntity
import me.hyungil.category.category.domain.hierachy.Hierarchy
import javax.persistence.*

@Entity
class Category(
    @Column(nullable = false)
    var name: String,

    @Embedded
    val hierarchy: Hierarchy,

    @Column(nullable = false)
    val isDeleted: Boolean,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    val id: Long = 0L

) : BaseTimeEntity() {

    constructor(name: String) : this(name, Hierarchy(), false)

    fun createRootCategory(rootCategory: Category) {
        hierarchy.createRootCategory(rootCategory)
    }

    fun createSubCategory(subCategory: Category) {
        hierarchy.createSubCategory(this, subCategory.hierarchy)
    }

    fun updateSubCategory(name: String, subCategory: Category) {
        subCategory.name = name
        hierarchy.updateSubCategory(this, subCategory.hierarchy)
    }

    fun getRootCategory() = hierarchy.getRootCategory()
    fun getDepth() = hierarchy.getDepth()
    fun getLeftNode() = hierarchy.getLeftNode()
    fun getRightNode() = hierarchy.getRightNode()
}