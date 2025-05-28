import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskCrudComponent } from './task-crud.component';

describe('TaskCrudComponent', () => {
  let component: TaskCrudComponent;
  let fixture: ComponentFixture<TaskCrudComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TaskCrudComponent]
    });
    fixture = TestBed.createComponent(TaskCrudComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
