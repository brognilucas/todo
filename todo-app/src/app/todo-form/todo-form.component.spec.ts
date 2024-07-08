import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { TodoFormComponent } from './todo-form.component';
import { FakeTodoService } from '../services/todo-fake.service';
import { TodoService } from '../services/todo.service';
describe('TodoFormComponent', () => {
  let component: TodoFormComponent;
  let fixture: ComponentFixture<TodoFormComponent>;
  let service: TodoService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TodoFormComponent],
      imports: [FormsModule],
      providers: [
        { provide: TodoService, useClass: FakeTodoService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TodoFormComponent);
    component = fixture.componentInstance;
    service = TestBed.inject(TodoService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should add a new todo', () => {
    const newTodo = { title: 'New Todo', description: 'Test Description', category: 'Work', completed: false };
    component.todo = newTodo;
    
    spyOn(service, 'createTodo').and.callThrough();
    component.submitTodoForm();
    
    expect(service.createTodo).toHaveBeenCalledWith(newTodo);
  });

  it('should disable "Create Todo" button if todo title is not filled in', () => {
    const compiled = fixture.nativeElement;
    const titleInput = compiled.querySelector('input[name="title"]');
    const createButton = compiled.querySelector('button[type="submit"]');

    expect(titleInput.value).toEqual('')

    expect(createButton.disabled).toBe(true);
  });


  it('should disable "Create Todo" button if category is not selected', () => {
    const compiled = fixture.nativeElement;
    const titleInput = compiled.querySelector('input[name="title"]');
    const categorySelect = compiled.querySelector('select[name="category"]');
    const createButton = compiled.querySelector('button[type="submit"]');
    titleInput.value = 'Test Todo';
    titleInput.dispatchEvent(new Event('input'));

    fixture.detectChanges();

    expect(titleInput.value).toEqual('Test Todo')
    expect(categorySelect.value).toEqual('');
    expect(createButton.disabled).toBe(true);
  });

  it('should enable "Create Todo" button if todo title and category are not empty ', () => {
    const compiled = fixture.nativeElement;
    const titleInput = compiled.querySelector('input[name="title"]');
    const categorySelect = compiled.querySelector('select[name="category"]');
    const createButton = compiled.querySelector('button[type="submit"]');
    titleInput.value = 'Test Todo';
    titleInput.dispatchEvent(new Event('input'));

    categorySelect.value = 'Personal';
    categorySelect.dispatchEvent(new Event('change'));

    fixture.detectChanges();

    expect(titleInput.value).toEqual('Test Todo')
    expect(categorySelect.value).toEqual('Personal');
    expect(createButton.disabled).toBe(false);
  });

});
