import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TodoListComponent } from './todo-list.component';
import { FakeTodoService } from '../services/todo-fake.service';
import { TodoService } from '../services/todo.service';
import { FormsModule } from '@angular/forms';

describe('TodoListComponent', () => {
  let component: TodoListComponent;
  let fixture: ComponentFixture<TodoListComponent>;
  let service: TodoService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TodoListComponent],
      providers: [
        { provide: TodoService, useClass: FakeTodoService },
      ],
      imports: [
        FormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TodoListComponent);
    component = fixture.componentInstance;
    service = TestBed.inject(TodoService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load todos on init', () => {
    expect(component.todos.length).toBe(2);
  });

  it('should mark a todo as completed', () => {
    spyOn(service, 'markTodoCompleted').and.callThrough();
    component.markAsCompleted(1);
    expect(service.markTodoCompleted).toHaveBeenCalledWith(1);
  });

  it('should filter todos by category', () => {
    spyOn(service, 'getAllTodos').and.callThrough();

    component.applyFilters('Work');
    expect(service.getAllTodos).toHaveBeenCalled();
    expect(component.selectedCategory).toBe('Work');
    expect(component.todos.length).toBe(1);

    component.applyFilters('Personal');
    expect(service.getAllTodos).toHaveBeenCalled();
    expect(component.selectedCategory).toBe('Personal');
    expect(component.todos.length).toBe(1);

    component.applyFilters('');
    expect(service.getAllTodos).toHaveBeenCalled();
    expect(component.selectedCategory).toBe('');
    expect(component.todos.length).toBe(2);
  });

  it('should show "Mark as Completed" button disabled for todos that are completed', () => {
    spyOn(service, 'getAllTodos').and.callThrough();
    const completedTodoIndex = component.todos.findIndex((todo) => todo.completed); 
    const buttons = fixture.nativeElement.querySelectorAll('button');
    expect(buttons[completedTodoIndex].disabled).toBe(true);
  })

  it('should get completed todos when checking "Show completed todos" checkbox', () => {
    const spy = spyOn(service, 'getAllTodos').and.callThrough();
    const compiled = fixture.nativeElement;
    const completedTodosCheckbox = compiled.querySelector('input[name="showCompleted"]');
    const userEvent = new MouseEvent('click');
    completedTodosCheckbox.dispatchEvent(userEvent);
    
    fixture.detectChanges();

    expect(spy).toHaveBeenCalledWith({ showCompleted: true, category: ''})
  })
});
