package me.hyungil.category.category.domain.category

import me.hyungil.category.category.domain.data.BaseTimeEntity
import me.hyungil.category.category.domain.hierachy.Hierarchy
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Category(

    @Column(nullable = false)
    var name: String,

    @Embedded
    @Column(nullable = false)
    val hierarchy: Hierarchy,

    @Column(nullable = false)
    val isDeleted: Boolean,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    val id: Long = 0L

) : BaseTimeEntity() {

    constructor(name: String) : this(name, Hierarchy(), false)

    fun createRootCategory(rootCategory: Category) {
        hierarchy.createRootCategory(rootCategory)
    }

    fun createSubCategory(subCategory: Category) {
        hierarchy.createSubCategory(this, subCategory.hierarchy)
    }

    fun updateCategoryName(name: String) {
        this.name = name
        this.modifiedDate = LocalDateTime.now()
    }

    fun getRootCategory() = hierarchy.getRootCategory()
    fun getLeftNode() = hierarchy.getLeftNode()
    fun getRightNode() = hierarchy.getRightNode()
}