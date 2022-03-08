package me.hyungil.category.category.domain.data

import java.time.LocalDateTime
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseTimeEntity {

    private val createdDate = LocalDateTime.now()

    var modifiedDate: LocalDateTime? = null
}