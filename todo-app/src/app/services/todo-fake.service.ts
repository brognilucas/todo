import { filter, Observable, of } from 'rxjs';
import { Todo } from '../model/todo';

export class FakeTodoService {
  private todos: Todo[] = [
    { id: 1, title: 'Finish Assessment', category: 'Work', completed: false },
    { id: 2, title: 'Take dog out', category: 'Personal', completed: true },
  ];

  getAllTodos(filters?: { category?: String, showCompleted?: boolean  }): Observable<Todo[]> {
    let todos = this.todos;
    if (filters?.category) {
      todos = todos.filter((todo) => todo.category === filters.category);
    } 

    return of(todos);
  }

  markTodoCompleted(todoId: number): Observable<any> {
    const todo = this.todos.find(t => t.id === todoId);
    if (todo) {
      todo.completed = true;
    }
    return of(todo);
  }

  createTodo(todo: Todo): Observable<Todo> {
    todo.id = this.todos.length + 1;
    this.todos.push(todo);
    return of(todo);
  }
}
