package com.brognilucas

import jakarta.inject.Singleton
import kotlinx.coroutines.runBlocking

@Singleton
class TodoService(private val todoRepository: TodoRepository) {

    suspend  fun getTodos(filters: TodoFilters): List<Todo> = runBlocking {
        val todos = todoRepository.findAll(filters)
        return@runBlocking todos;
    }

    suspend fun getTodoById(id: Long): Todo = runBlocking {
        val todo = todoRepository.findById(id)
        if (todo == null) {
            throw Exception("Todo not found");
        }
        return@runBlocking todo;
    }

    suspend fun createTodo(todo: Todo): Todo {
        val todo = todoRepository.save(todo)
        return todo;
    }

    suspend fun markAsDone(todoId: Long): Todo {
        val todo = getTodoById(todoId);
        todo.markAsCompleted();

        todoRepository.update(todo);

        return todo;
    }
}
