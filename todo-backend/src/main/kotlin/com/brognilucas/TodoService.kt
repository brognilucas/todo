package com.brognilucas

import jakarta.inject.Singleton

@Singleton
class TodoService(private val todoRepository: TodoRepository) {

    fun getTodos(filters: TodoFilters): List<Todo> {
        val todos = todoRepository.findAll(filters)
        return todos;
    }

    fun getTodoById(id: Long): Todo {
        val todo = todoRepository.findById(id)
        if (todo == null) {
            throw Exception("Todo not found");
        }
        return todo;
    }

    fun createTodo(todo: Todo): Todo {
        val todo = todoRepository.save(todo)
        return todo;
    }

    fun markAsDone(todoId: Long): Todo {
        val todo = getTodoById(todoId);
        todo.markAsCompleted();

        todoRepository.update(todo);

        return todo;
    }
}
