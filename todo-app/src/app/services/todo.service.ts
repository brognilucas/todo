import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Todo } from '../model/todo';

@Injectable({
  providedIn: 'root'
})
export class TodoService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getAllTodos(params?: { category?: string; showCompleted?: boolean }): Observable<Todo[]> {
    let queryParams = new HttpParams();
    if (params) {
      if (params.category) {
        queryParams = queryParams.set('category', params.category);
      }
      if (params.showCompleted !== undefined) {
        queryParams = queryParams.set('showCompleted', params.showCompleted.toString());
      }
    }

    return this.http.get<Todo[]>(`${this.apiUrl}/todos`, { params: queryParams });
  }


  markTodoCompleted(todoId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/todos/${todoId}/complete`, {});
  }

  createTodo(todo: Todo): Observable<Todo> {
    return this.http.post<Todo>(`${this.apiUrl}/todos`, todo);
  }
}
