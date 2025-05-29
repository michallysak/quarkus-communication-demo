import { Observable } from 'rxjs';
import { CreateTask, Task } from './task.model';

export interface TaskServiceInterface {
  getTasks(): Observable<Task[]>;
  addTask(task: CreateTask): Observable<Task>;
  updateTask(task: Task): Observable<Task>;
  deleteTask(id: string): Observable<void>;
  deleteAllTasks(): Observable<void>;
}
