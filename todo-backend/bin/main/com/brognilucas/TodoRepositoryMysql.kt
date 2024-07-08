package com.brognilucas

import io.micronaut.context.annotation.Property
import jakarta.inject.Singleton
import java.sql.Connection
import java.sql.DriverManager

@Singleton
class TodoRepositoryMysql(
    @Property(name = "datasources.default.url") private val jdbcUrl: String,
    @Property(name = "datasources.default.username") private val dbUser: String,
    @Property(name = "datasources.default.password") private val dbPassword: String
) : TodoRepository {

    private fun getConnection(): Connection {
        return DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)
    }

    private fun buildQuery(filters: TodoFilters?): String {
        val baseQuery = "SELECT id, title, category, description, completed FROM todo"
        val conditions = mutableListOf<String>()
        if (filters?.category != null) {
            conditions.add("category = ?")
        }
        if (filters?.title != null) {
            conditions.add("title LIKE ?")
        }

        if (!filters?.showCompleted!!) {
            conditions.add("completed = false")
        }

        return when {
            conditions.isEmpty() -> baseQuery
            else -> "$baseQuery WHERE ${conditions.joinToString(" AND ")}"
        }
    }


    override fun findAll(todoFilters: TodoFilters?): List<Todo> {
        val todos = mutableListOf<Todo>()
        getConnection().use { conn ->
            val sql = buildQuery(todoFilters)
            val stmt = conn.prepareStatement(sql)
            var index = 1
            if (todoFilters?.category != null) {
                stmt.setString(index++, todoFilters.category)
            }
            if (todoFilters?.title != null) {
                stmt.setString(index++, "%${todoFilters.title}%")
            }
            val rs = stmt.executeQuery()
            while (rs.next()) {
                todos.add(
                    Todo(
                        id = rs.getLong("id"),
                        title = rs.getString("title"),
                        category = rs.getString("category"),
                        description = rs.getString("description"),
                        completed = rs.getBoolean("completed")
                    )
                )
            }
        }
        return todos
    }

    override fun findById(id: Long): Todo? {
        getConnection().use { conn ->
            val stmt = conn.prepareStatement("SELECT * FROM todo WHERE id = ?")
            stmt.setLong(1, id)
            val rs = stmt.executeQuery()
            return if (rs.next()) {
                Todo(
                    id = rs.getLong("id"),
                    title = rs.getString("title"),
                    category = rs.getString("category"),
                    description = rs.getString("description"),
                    completed = rs.getBoolean("completed")
                )
            } else {
                null
            }
        }
    }

    override fun save(todo: Todo): Todo {
        getConnection().use { conn ->
            val stmt = conn.prepareStatement(
                "INSERT INTO todo (title, category, description, completed) VALUES (?, ?, ?, ?)",
                java.sql.Statement.RETURN_GENERATED_KEYS
            )
            stmt.setString(1, todo.title)
            stmt.setString(2, todo.category)
            stmt.setString(3, todo.description)
            stmt.setBoolean(4, todo.completed)
            stmt.executeUpdate()
            val rs = stmt.generatedKeys
            if (rs.next()) {
                return  Todo(
                    id = rs.getLong(1),
                    title = todo.title,
                    category = todo.category,
                    description = todo.description,
                    completed = todo.completed
                )
            }
        }
        throw RuntimeException("Failed to insert todo")
    }

    override fun update(todo: Todo): Todo {
        getConnection().use { conn ->
            val stmt = conn.prepareStatement(
                "UPDATE todo SET title = ?, description = ?, category = ?, completed = ? WHERE id = ?"
            )
            stmt.setString(1, todo.title)
            stmt.setString(2, todo.description)
            stmt.setString(3, todo.category)
            stmt.setBoolean(4, todo.completed)
            stmt.setLong(5, todo.id!!)
            stmt.executeUpdate()
        }
        return todo
    }

}
