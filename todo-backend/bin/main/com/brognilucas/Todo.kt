package com.brognilucas

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@Introspected
class Todo(
    var id: Long? = null,
    val title: String,
    val category: String,
    val description: String? = null,
    var completed: Boolean = false
) {

    fun markAsCompleted() {
        completed = true;
    }
}
