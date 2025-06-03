import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateTask, Task } from './task.model';
import { TaskServiceInterface } from './task.service.interface';

@Injectable({
  providedIn: 'root',
})
export class TaskRestService implements TaskServiceInterface {
  readonly apiBaseUrl = 'http://localhost:8080';
  readonly taskApiUrl = `${this.apiBaseUrl}/task`;

  constructor(private readonly http: HttpClient) {}

  getTask(taskId: string): Observable<Task> {
    return this.http.get<Task>(`${this.taskApiUrl}/${taskId}`);
  }

  getTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(this.taskApiUrl);
  }

  addTask(task: CreateTask): Observable<Task> {
    return this.http.post<Task>(this.taskApiUrl, task);
  }

  updateTask(task: Task): Observable<Task> {
    return this.http.put<Task>(`${this.taskApiUrl}/${task.id}`, task);
  }

  deleteTask(id: string): Observable<void> {
    return this.http.delete<void>(`${this.taskApiUrl}/${id}`)
  }

  deleteAllTasks(): Observable<void> {
    return this.http.delete<void>(`${this.taskApiUrl}`);
  }

  processTask(taskId: string): Observable<void> {
    return this.http.patch<void>(`${this.taskApiUrl}/${taskId}/process`, {});
  }

  longPoll(taskId: string): Observable<Task> {
    return this.http.get<Task>(`${this.taskApiUrl}/${taskId}/long-poll`);
  }
}
