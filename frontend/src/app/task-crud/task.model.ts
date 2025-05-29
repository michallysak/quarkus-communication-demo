export interface CreateTask {
  name: string;
}

export type Task = CreateTask & {
  id: string;
}
