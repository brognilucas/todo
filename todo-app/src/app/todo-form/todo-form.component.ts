import { Component } from '@angular/core';
import { Todo } from '../model/todo';
import { TodoService } from '../services/todo.service';

@Component({
  selector: 'app-todo-form',
  templateUrl: './todo-form.component.html',
  styleUrls: ['./todo-form.component.css']
})
export class TodoFormComponent {
  todo: Todo = {
    title: '',
    category: '',
    completed: false
  };

  constructor(private todoService: TodoService) {}

  submitTodoForm() {
    this.todoService.createTodo(this.todo).subscribe(() => {
      this.todo = { title: '', category: '', completed: false };
    });
  }
}
