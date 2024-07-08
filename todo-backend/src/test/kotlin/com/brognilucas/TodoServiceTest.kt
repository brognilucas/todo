package com.brognilucas

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TodoServiceTest {

    private lateinit var todoRepository: FakeTodoRepository
    private lateinit var todoService: TodoService

    private lateinit var todo: Todo

    @BeforeEach
    fun setUp() {
        todoRepository = FakeTodoRepository()
        todoService = TodoService(todoRepository)

        todo = Todo(
            title = "Title",
            category = "Category",
            description = "Description"
        )
    }

    @Test
    fun `should be able to get a list of todos`() {
        todoRepository.save(todo)

        val todos = todoService.getTodos(TodoFilters())

        assertNotNull(todos)
        assertEquals(1, todos.size)
        assertEquals("Title", todos[0].title)
    }

    @Test
    fun `should be able to get a todo by id`() {
        todoRepository.save(todo)

        val fetchedTodo = todoService.getTodoById(1L)

        assertNotNull(fetchedTodo)
        assertEquals("Title", fetchedTodo.title)
    }

    @Test
    fun `should throw when getting a todo that doesnt exists`() {
        val exception = assertThrows<Exception> {
            todoService.getTodoById(1L)
        }

        assertEquals("Todo not found", exception.message)
    }

    @Test
    fun `should be able to create a todo`() {
        val createdTodo = todoService.createTodo(todo)

        assertNotNull(createdTodo)
        assertEquals("Title", createdTodo.title)
    }

    @Test
    fun `should mark todo as completed`() {
        val createdTodo = todoRepository.save(todo)

        todoService.markAsDone(createdTodo.id!!)

        val updatedTodo = todoRepository.findById(createdTodo.id!!);

        assertEquals(updatedTodo?.id, createdTodo.id);
        assertEquals(updatedTodo?.completed, true);
    }

    @Test
    fun `should filter todos by category`() {
        todoService.createTodo(Todo(title = "title1", description = "description1", category = "work"));
        todoService.createTodo(Todo(title = "title2", description = "description2", category = "work"));
        todoService.createTodo(Todo(title = "title3", description = "description3", category = "personal"));

        val workTodos = todoService.getTodos(TodoFilters(category = "work"))
        assertEquals(2, workTodos.size)
        assertEquals("work", workTodos[0].category)
        assertEquals("work", workTodos[1].category)

        val personalTodos = todoService.getTodos(TodoFilters(category = "personal"))
        assertEquals(1, personalTodos.size)
        assertEquals("personal", personalTodos[0].category)
    }

    @Test
    fun `should filter todos by title`() {
        todoService.createTodo(Todo(title = "title1", description = "description1", category = "work"));
        todoService.createTodo(Todo(title = "title2", description = "description2", category = "work"));
        todoService.createTodo(Todo(title = "title3", description = "description3", category = "personal"));
        val todos = todoService.getTodos(TodoFilters(title = "title1"))
        assertEquals(1, todos.size)
        assertEquals(todos.get(0).title, "title1")
    }

    @Test
    fun `should not list completed todos by default`() {
        val todo = todoService.createTodo(Todo(title = "title1", description = "description1", category = "work"));
        val todo2 = todoService.createTodo(Todo(title = "title2", description = "description2", category = "work"));

        todoService.markAsDone(todo2.id!!)

        val todos = todoService.getTodos(TodoFilters());

        assertEquals(1, todos.size)
        assertEquals(todos.get(0).id, todo.id);
    }
}
