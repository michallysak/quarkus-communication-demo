import { Observable } from 'rxjs';
import { CreateTask, Task } from './task.model';

export interface TaskServiceInterface {
  getTask(taskId: string): Observable<Task>;
  getTasks(): Observable<Task[]>;
  addTask(task: CreateTask): Observable<Task>;
  updateTask(task: Task): Observable<Task>;
  deleteTask(id: string): Observable<void>;
  deleteAllTasks(): Observable<void>;
  processTask(taskId: string): Observable<void>;
}
