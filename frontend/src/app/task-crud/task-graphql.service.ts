import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { CreateTask, Task } from './task.model';
import { TaskServiceInterface } from './task.service.interface';
import { Apollo, gql, MutationResult } from 'apollo-angular';
import { ApolloQueryResult } from '@apollo/client/core';


function runCatching<T, U>(result: ApolloQueryResult<T> | MutationResult<T>, mapper: (a: NonNullable<T>) => U): U {
  if (result.errors) {
    throw new Error(
      `GraphQL errors: ${result.errors.map((e: any) => e.message).join(', ')}`
    );
  }
  if (!result.data) {
    throw new Error('No data returned from GraphQL operation');
  }
  return mapper(result.data);
}


@Injectable({
  providedIn: 'root',
})
export class TaskGraphQLService implements TaskServiceInterface {
  constructor(private readonly apollo: Apollo) {}

  getTasks(): Observable<Task[]> {
    const query = gql`
      query {
        allTasks {
          id
          name
        }
      }
    `;
    return this.apollo
      .watchQuery<{ allTasks: Task[] }>({ query })
      .valueChanges.pipe(map((result) => runCatching(result, (data) => data.allTasks)));
  }

  addTask(task: CreateTask): Observable<Task> {
    const mutation = gql`
      mutation {
        createTask(taskCreateDto: { name: "${task.name}" }) {
          id
          name
        }
      }
    `;
    return this.apollo
      .mutate<{ createTask: Task }>({ mutation })
      .pipe(map((result) => runCatching(result, (data) => data.createTask)));
  }

  updateTask(task: Task): Observable<Task> {
    const mutation = gql`
      mutation {
        updateTask(id: "${task.id}", taskDto: { name: "${task.name}" }) {
          id
          name
        }
      }
    `;
    return this.apollo
      .mutate<{ updateTask: Task }>({ mutation })
      .pipe(map((result) => runCatching(result, (data) => data.updateTask)));
  }

  deleteTask(id: string): Observable<void> {
    const mutation = gql`
      mutation {
        deleteTask(id: "${id}")
      }
    `;
    return this.apollo
      .mutate<{ deleteTask: boolean }>({ mutation })
      .pipe(map((result) => runCatching(result, () => {})));
  }

  deleteAllTasks(): Observable<void> {
    const mutation = gql`
      mutation {
        deleteAllTasks
      }
    `;
    return this.apollo
      .mutate<{ deleteTask: boolean }>({ mutation })
      .pipe(map((result) => runCatching(result, () => {})));
  }
}
