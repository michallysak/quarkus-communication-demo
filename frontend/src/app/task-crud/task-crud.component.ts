import { Component, OnInit } from '@angular/core';
import { Task } from './task.model';
import { TaskService } from './task.service';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-task-crud',
  templateUrl: './task-crud.component.html',
})
export class TaskCrudComponent implements OnInit {
  newTask: Task = { name: '' };
  editTask: Task | null = null;

  constructor(public readonly taskService: TaskService) {}

  ngOnInit() {
    this.taskService.getTasks().subscribe();
  }

  addTask() {
    this.taskService
      .addTask(this.newTask)
      .pipe(
        catchError((err) => {
          alert('Error adding task: ' + err.message);
          return of(null);
        })
      )
      .subscribe(() => {
        this.taskService.getTasks().subscribe();
        this.newTask = { name: '' };
      });
  }

  startEdit(task: Task) {
    this.editTask = { ...task };
  }

  updateTask() {
    if (!this.editTask?.id) return;
    this.taskService
      .updateTask(this.editTask)

      .pipe(
        catchError((err) => {
          alert('Error updating task: ' + err.message);
          return of(null);
        })
      )
      .subscribe(() => {
        this.taskService.getTasks().subscribe();
        this.editTask = null;
      });
  }

  deleteTask(id: string) {
    this.taskService
      .deleteTask(id)
      .pipe(
        catchError((err) => {
          alert('Error deleting task: ' + err.message);
          return of(null);
        })
      )
      .subscribe(() => this.taskService.getTasks().subscribe());
  }

  cancelEdit() {
    this.editTask = null;
  }

}
