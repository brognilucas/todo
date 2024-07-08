package com.brognilucas

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertInstanceOf

@MicronautTest
class TodoTest {
    @Test
    fun `should create a valid instance of todo`() {
        val todo = Todo(
            id = 1,
            title = "title",
            description = "description",
            category = "category"
        )

        assertInstanceOf(Todo::class.java, todo)
        assertEquals(todo.id, 1);
        assertEquals(todo.title, "title");
        assertEquals(todo.description, "description");
        assertEquals(todo.category, "category");
    }

    @Test
    fun `should initialize todo with PENDING status`() {
        val todo = Todo(
            id = 1,
            title = "title",
            description = "description",
            category = "category"
        );

        assertEquals(todo.completed, false);
    }

    @Test
    fun `should be able to mark a todo as done`(){
        val todo = Todo(
            id = 1,
            title = "title",
            description = "description",
            category = "category"
        );

        todo.markAsCompleted();

        assertEquals(todo.completed, true);
    }
}