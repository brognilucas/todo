package com.brognilucas
import io.micronaut.http.annotation.*

@Controller("/todos")
class TodoController(private val todoService: TodoService) {

    @Get("/")
    fun listTodos(
        @QueryValue category: String?,
        @QueryValue title: String?,
        @QueryValue(defaultValue = "false") showCompleted: Boolean,
    ): List<Todo> {
        return todoService.getTodos(TodoFilters(
            category = category,
            title = title,
            showCompleted = showCompleted
        ))
    }

    @Post("/")
    fun createTodo(@Body todo: Todo): Todo {
        return todoService.createTodo(todo)
    }

    @Put("/{id}/complete")
    fun completeTodo(@PathVariable("id") todoId: Long): Todo {
        return todoService.markAsDone(todoId);
    }
}
