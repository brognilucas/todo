package com.brognilucas
class FakeTodoRepository : TodoRepository {
    private val todos = mutableListOf<Todo>()

    override fun findAll(todoFilters: TodoFilters?): List<Todo> {
        var todoList = todos;
        if (todoFilters?.category != null) {
            todoList = todos.filter { it -> it.category == todoFilters.category }.toMutableList()
        }

        if (todoFilters?.title != null) {
            todoList = todos.filter { it -> it.title.contains(todoFilters.title!!) }.toMutableList()
        }
        return todoList
    }

    override fun findById(id: Long): Todo? {
        return todos.find { it.id == id }
    }

    override fun save(todo: Todo): Todo {
        todos.add(todo)
        return todo
    }

    override fun update(todo: Todo): Todo {
        val index = todos.indexOfFirst { it.id == todo.id }
        if (index != -1) {
            todos[index] = todo
        }
        return todos[index];
    }

    fun clear() {
        todos.clear()
    }
}
