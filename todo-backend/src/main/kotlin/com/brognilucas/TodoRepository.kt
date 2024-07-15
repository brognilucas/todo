package com.brognilucas

interface TodoRepository {
    suspend fun findAll(todoFilters: TodoFilters?): List<Todo>
    suspend fun findById(id: Long): Todo?
    suspend fun save(todo: Todo): Todo
    suspend fun update(todo: Todo): Todo
}
