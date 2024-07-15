package com.brognilucas
import io.micronaut.http.annotation.*
import kotlinx.coroutines.runBlocking

@Controller("/todos")
class TodoController(private val todoService: TodoService) {

    @Get("/")
    suspend fun listTodos(
        @QueryValue category: String?,
        @QueryValue title: String?,
        @QueryValue(defaultValue = "false") showCompleted: Boolean,
    ): List<Todo>  {
        return todoService.getTodos(TodoFilters(
            category = category,
            title = title,
            showCompleted = showCompleted
        ))
    }

    @Post("/")
    suspend fun createTodo(@Body todo: Todo): Todo {
        return todoService.createTodo(todo)
    }

    @Put("/{id}/complete")
    suspend fun completeTodo(@PathVariable("id") todoId: Long): Todo {
        return todoService.markAsDone(todoId);
    }
}
