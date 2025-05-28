import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Task } from './task.model';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class TaskService {
  readonly apiBaseUrl = 'http://localhost:8080';
  readonly taskApiUrl = `${this.apiBaseUrl}/task`;
  tasks: Task[] = [];

  constructor(private readonly http: HttpClient) {}

  getTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(this.taskApiUrl).pipe(
      tap(data => this.tasks = data)
    );
  }

  addTask(task: Task): Observable<void> {
    return this.http.post<void>(this.taskApiUrl, task);
  }

  updateTask(task: Task): Observable<void> {
    return this.http.put<void>(`${this.taskApiUrl}/${task.id}`, task);
  }

  deleteTask(id: string): Observable<void> {
    return this.http.delete<void>(`${this.taskApiUrl}/${id}`);
  }
}
