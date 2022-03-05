package me.hyungil.category.category.domain.category

import me.hyungil.category.category.domain.BaseTimeEntity
import me.hyungil.category.category.domain.hierachy.Hierarchy
import javax.persistence.*

@Entity
class Category(
    @Column(nullable = false)
    val name: String,

    @Embedded
    val hierarchy: Hierarchy = Hierarchy(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    val id: Long = 0L

) : BaseTimeEntity() {

    fun updateRootCategory(rootCategory: Category) {
        hierarchy.updateRootCategory(rootCategory)
    }

    fun updateParentCategory(parentCategory: Category) {
        hierarchy.updateParentCategory(parentCategory)
    }

    fun updateSubCategory(subCategory: Category) {
        hierarchy.updateUpdateSubCategory(subCategory)
    }

    fun getDepth() = hierarchy.getDepth()
}