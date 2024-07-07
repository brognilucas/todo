package com.brognilucas

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@Introspected
class Todo(
    val id: Long? = null,
    val title: String,
    val category: String,
    val description: String?,
    var completed: Boolean = false
) {

    fun markAsCompleted() {
        completed = true;
    }
}
