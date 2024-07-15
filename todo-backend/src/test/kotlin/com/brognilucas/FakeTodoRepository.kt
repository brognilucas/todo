package com.brognilucas
class FakeTodoRepository : TodoRepository {
    private val todos = mutableListOf<Todo>()

    override suspend fun findAll(todoFilters: TodoFilters?): List<Todo> {
        var todoList = todos;
        if (todoFilters?.category != null) {
            todoList = todoList.filter { it -> it.category == todoFilters.category }.toMutableList()
        }

        if (todoFilters?.title != null) {
            todoList = todoList.filter { it -> it.title.contains(todoFilters.title!!) }.toMutableList()
        }

        if (todoFilters?.showCompleted == false) {
            todoList = todoList.filter { it -> !it.completed  }.toMutableList()
        }

        return todoList
    }

    override suspend fun findById(id: Long): Todo? {
        return todos.find { it.id == id }
    }

    override suspend fun save(todo: Todo): Todo {
        val todoCopy = Todo (id = (todos.size + 1).toLong(), title = todo.title, description = todo.description, category = todo.category, completed = false)
        todos.add(todoCopy)
        return todoCopy
    }

    override suspend fun update(todo: Todo): Todo {
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
