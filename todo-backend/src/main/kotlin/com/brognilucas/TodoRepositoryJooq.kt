package com.brognilucas

import io.micronaut.transaction.annotation.ReadOnly
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton
import org.jooq.DSLContext
import org.jooq.impl.DSL
import reactor.kotlin.core.publisher.toMono

@Singleton
open class TodoRepositoryJooq(
    private val dsl: DSLContext
) : TodoRepository {

    @ReadOnly
    @Transactional
    override suspend fun findAll(todoFilters: TodoFilters?): List<Todo> {
        val query = dsl.selectFrom(DSL.table("todo"))
        val conditions = mutableListOf<org.jooq.Condition>()

        todoFilters?.category?.let {
            conditions.add(DSL.field("category").eq(it))
        }
        todoFilters?.title?.let {
            conditions.add(DSL.field("title").like("%$it%"))
        }
        if (todoFilters?.showCompleted == false) {
            conditions.add(DSL.field("completed").eq(false))
        }
        if (conditions.isNotEmpty()) {
            query.where(conditions)
        }

        return query.map { it -> it.into(Todo::class.java) }
    }

    @ReadOnly
    @Transactional
    override suspend fun findById(id: Long): Todo? {
        val record = dsl.selectFrom(DSL.table("todo"))
            .where(DSL.field("id").eq(id))
            .fetchOne()

        return record?.into(Todo::class.java)
    }

    @Transactional
    override suspend fun save(todo: Todo): Todo {
        val record = dsl.newRecord(DSL.table("todo"))
        record.set(DSL.field("title"), todo.title)
        record.set(DSL.field("category"), todo.category)
        record.set(DSL.field("description"), todo.description)
        record.set(DSL.field("completed"), todo.completed)

        val id = dsl.lastID().toLong();
        return Todo(id = id, title = todo.title, category = todo.category, description = todo.description, completed = todo.completed )
    }

    @Transactional
    override suspend fun update(todo: Todo): Todo {
        val record = dsl.newRecord(DSL.table("todo"))
        record.set(DSL.field("title"), todo.title)
        record.set(DSL.field("category"), todo.category)
        record.set(DSL.field("description"), todo.description)
        record.set(DSL.field("completed"), todo.completed)
        record.set(DSL.field("id"), todo.id)
        return todo
    }
}
