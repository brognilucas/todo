import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TodoService } from './todo.service';
import { Todo } from '../model/todo';

describe('TodoService', () => {
  let service: TodoService;
  let httpMock: HttpTestingController;

  const API_URL = 'http://localhost:8080';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TodoService]
    });

    service = TestBed.inject(TodoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return all todos', () => {
    const mockTodos: Todo[] = [
      { id: 1, title: 'Finish Assessment', category: 'Work', completed: false },
      { id: 2, title: 'Take dog to walk', category: 'Personal', completed: false }
    ];

    service.getAllTodos().subscribe(todos => {
      expect(todos.length).toBe(2);
      expect(todos).toEqual(mockTodos);
    });

    const req = httpMock.expectOne(`${API_URL}/todos`);
    expect(req.request.method).toBe('GET');
    req.flush(mockTodos);
  });

  it('should mark todo as completed', () => {
    const todoId = 1;
    const mockTodo: Todo = { id: todoId, title: 'Finish Assessment', category: 'Work', completed: true };

    service.markTodoCompleted(todoId).subscribe(todo => {
      expect(todo.completed).toBeTrue();
    });

    const req = httpMock.expectOne(`${API_URL}/todos/${todoId}/complete`);
    expect(req.request.method).toBe('PUT');
    req.flush(mockTodo);
  });

  it('should create a new todo', () => {
    const newTodo: Todo = { title: 'Make pasta for lunch', category: 'Personal', completed: false };

    service.createTodo(newTodo).subscribe(todo => {
      expect(todo.title).toBe(newTodo.title);
    });

    const req = httpMock.expectOne(`${API_URL}/todos`);
    expect(req.request.method).toBe('POST');
    req.flush(newTodo);
  });
});
