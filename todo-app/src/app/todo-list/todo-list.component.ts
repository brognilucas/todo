import { Component, OnInit } from '@angular/core';
import { Todo } from '../model/todo';
import { TodoService } from '../services/todo.service';

@Component({
  selector: 'app-todo-list',
  templateUrl: './todo-list.component.html',
  styleUrls: ['./todo-list.component.css']
})
export class TodoListComponent implements OnInit {
  todos: Todo[] = [];

  selectedCategory: string = '';
  showCompleted: boolean = false;

  categories = ['Work', 'Personal']

  constructor(private todoService: TodoService) { }

  ngOnInit() {
    this.fetchTodos();
  }

  fetchTodos(filters?: { category?: string, showCompleted?: boolean }) {
    this.todoService.getAllTodos(filters).subscribe(todos => {
      this.todos = todos;
    });
  }

  markAsCompleted(todoId?: number) {
    if (!todoId) throw new Error('Invalid todo id');

    this.todoService.markTodoCompleted(todoId).subscribe(() => {
      this.fetchTodos();
    });
  }

  applyFilters(category: string, showCompleted: boolean = false) { 
    this.selectedCategory = category;
    this.showCompleted = showCompleted;

    this.fetchTodos({ category, showCompleted });
  }
}
