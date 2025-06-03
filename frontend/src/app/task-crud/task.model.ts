export type TaskStatus = 'CREATED' | 'IN_PROGRESS' | 'COMPLETED'

export interface CreateTask {
  name: string;
}

export type Task = CreateTask & {
  id: string;
  status: TaskStatus;
}
