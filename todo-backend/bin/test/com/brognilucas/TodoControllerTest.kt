package com.brognilucas

import io.micronaut.context.annotation.Property
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.DriverManager

@MicronautTest
class TodoControllerTest(private val embeddedServer: EmbeddedServer) {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient


    @Property(name = "datasources.default.url")
    lateinit var jdbcUrl: String

    @Property(name = "datasources.default.username")
    lateinit var dbUser: String

    @Property(name = "datasources.default.password")
    lateinit var dbPassword: String

    private fun getConnection(): Connection {
        return DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)
    }

    fun callCreateTodo(todo: Todo): HttpResponse<Todo> {
        val request: HttpRequest<Todo> = HttpRequest.POST("/todos", todo)
        val response: HttpResponse<Todo> = client.toBlocking().exchange(request, Todo::class.java)
        return response;
    }

    @BeforeEach
    fun setup() {
        getConnection().use { conn ->
            val stmt = conn.createStatement()
            stmt.execute("DELETE FROM todo")
            stmt.close()
        }
    }

    @Test
    fun testServerIsRunning() {
        assert(embeddedServer.isRunning())
    }

    @Test
    fun testIndex() {
        val response: HttpResponse<String> = client.toBlocking().exchange("/todos", String::class.java)
        assertEquals(HttpStatus.OK, response.status)
    }

    @Test
    fun `POST todos should create a new todo`() {
        val todo = Todo(title = "New Task", category = "Work")
        val response = callCreateTodo(todo);
        assertEquals(HttpStatus.OK, response.status)
        assertEquals("New Task", response.body()?.title)
    }
    fun `GET todos should get a list of todos`() {
        // Create some todos
        val todo1 = Todo(
            title = "Task 1",
            category = "Work",
            description = "Description 1",
            completed = false
        )
        val todo2 = Todo(
            title = "Task 2",
            category = "Personal",
            description = "Description 2",
            completed = false
        )

        callCreateTodo(todo1);
        callCreateTodo(todo2);

        val response: HttpResponse<List<Todo>> = client.toBlocking().exchange(HttpRequest.GET<Any>("/todos"), Argument.listOf(Todo::class.java))
        val todos = response.body()

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(2, todos?.size)
    }

    @Test
    fun `GET todos?category=personal should get a list of todos filtered by category`() {
        val todo1 = Todo(
            title = "Task 1",
            category = "Work",
            description = "Description 1",
            completed = false
        )
        val todo2 = Todo(
            title = "Task 2",
            category = "Personal",
            description = "Description 2",
            completed = false
        )


        callCreateTodo(todo1);
        callCreateTodo(todo2);


        val response: HttpResponse<List<Todo>> = client.toBlocking()
            .exchange(HttpRequest.GET<Any>("/todos?category=Personal"), Argument.listOf(Todo::class.java))
        val todos = response.body()

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(1, todos?.size)
        assertEquals("Personal", todos?.first()?.category)
    }

    @Test
    fun `GET todos?showCompleted=true should get a list including completed todos`() {
        // Create some todos
        val incompleteTodo = Todo(
            title = "Incomplete Task",
            category = "Work",
            description = "Description 1",
            completed = false
        )
        val completeTodo = Todo(
            title = "Completed Task",
            category = "Personal",
            description = "Description 2",
            completed = true
        )

        callCreateTodo(incompleteTodo)
        callCreateTodo(completeTodo);

        val response: HttpResponse<List<Todo>> = client.toBlocking().exchange(HttpRequest.GET<Any>("/todos?showCompleted=true"), Argument.listOf(Todo::class.java))
        val todos = response.body()

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(2, todos?.size)
        assertEquals(true, todos?.any { it.completed })
    }


    // _ = / (/ is invalid for naming of a method)
    @Test
    fun `PUT todos_id_complete should mark todo as complete`() {
        val todo = Todo(title = "Task to Complete", category = "Personal")
        val createRequest: HttpRequest<Todo> = HttpRequest.POST("/todos", todo)
        val createResponse: HttpResponse<Todo> = client.toBlocking().exchange(createRequest, Todo::class.java)
        val createdTodo: Todo = createResponse.body()!!

        val markAsCompleteRequest: HttpRequest<Void> = HttpRequest.PUT("/todos/${createdTodo.id}/complete", null)
        val markAsCompleteResponse: HttpResponse<Todo> = client.toBlocking().exchange(markAsCompleteRequest, Todo::class.java)

        assertEquals(HttpStatus.OK, markAsCompleteResponse.status)
        assertEquals(true, markAsCompleteResponse.body()?.completed)
    }
}
