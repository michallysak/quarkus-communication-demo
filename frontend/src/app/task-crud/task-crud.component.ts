import { Component, OnInit } from '@angular/core';
import { CreateTask, Task } from './task.model';
import { TaskGraphQLService } from './task-graphql.service';
import { TaskRestService } from './task.service';
import { TaskServiceInterface } from './task.service.interface';
import { catchError, Observable, of } from 'rxjs';

type TaskAction = 'fetching' | 'adding' | 'updating' | 'deleting';

@Component({
  standalone: false,
  selector: 'app-task-crud',
  templateUrl: './task-crud.component.html',
})
export class TaskCrudComponent implements OnInit {
  tasks: Task[] = [];
  newTask: CreateTask = { name: '' };
  editTask: Task | null = null;
  useGraphQL: boolean = true;

  constructor(
    private readonly restService: TaskRestService,
    private readonly graphQLService: TaskGraphQLService
  ) {}

  private get taskService(): TaskServiceInterface {
    return this.useGraphQL ? this.graphQLService : this.restService;
  }

  ngOnInit() {
    this.loadTasks('fetching');
  }

  private loadTasks(action: TaskAction) {
    if (action === 'adding') {
      this.newTask = { name: '' };
    }
    if (action === 'updating') {
      this.cancelEdit();
    }
    this.taskService.getTasks().subscribe((data) => (this.tasks = data));
  }

  addTask() {
    this.handle(this.taskService.addTask(this.newTask), 'adding');
  }

  startEdit(id: string) {
    const task = this.tasks.find((task) => task.id === id);
    if (!task) return;
    this.editTask = { ...task };
  }

  updateTask() {
    if (!this.editTask?.id) return;
    this.handle(this.taskService.updateTask(this.editTask), 'updating');
  }

  deleteTask(id: string) {
    this.handle(this.taskService.deleteTask(id), 'deleting');
  }

  cancelEdit() {
    this.editTask = null;
  }

  handle<T>(observable: Observable<T>, action: TaskAction) {
    observable
      .pipe(
        catchError((err) => {
          alert(`Error ${action} task: ${err.message}`);
          return of(null);
        })
      )
      .subscribe(() => this.loadTasks(action));
  }
}
