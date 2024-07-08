package com.brognilucas

interface TodoRepository {
    fun findAll(todoFilters: TodoFilters?): List<Todo>
    fun findById(id: Long): Todo?
    fun save(todo: Todo): Todo
    fun update(todo: Todo): Todo
}
