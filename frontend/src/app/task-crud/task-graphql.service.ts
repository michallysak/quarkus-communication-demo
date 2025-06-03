import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { CreateTask, Task } from './task.model';
import { TaskServiceInterface } from './task.service.interface';
import { Apollo, gql } from 'apollo-angular';
import { ApolloClient, FetchResult } from '@apollo/client/core';
import { GRAPHQL_SUBSCRIPTION_CLIENT } from '../graphql/graphql.provider';

interface TaskUpdatesResponse {
  taskUpdates: Task;
}

function runCatching<T, U>(
  result: FetchResult<T>,
  mapper: (a: NonNullable<T>) => U
): U {
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
  constructor(
    private readonly apollo: Apollo,
    @Inject(GRAPHQL_SUBSCRIPTION_CLIENT)
    private readonly subscriptionClient: ApolloClient<any>
  ) {}

  getTask(taskId: string): Observable<Task> {
    const query = gql`
      query {
        taskById(id: "${taskId}") {
          id
          name
          status
        }
      }`;

    return this.apollo
      .watchQuery<{ taskById: Task }>({ query })
      .valueChanges.pipe(
        map((result) => runCatching(result, (data) => data.taskById))
      );
  }

  getTasks(): Observable<Task[]> {
    const query = gql`
      query {
        allTasks {
          id
          name
          status
        }
      }
    `;
    return this.apollo
      .watchQuery<{ allTasks: Task[] }>({ query })
      .valueChanges.pipe(
        map((result) => runCatching(result, (data) => data.allTasks))
      );
  }

  addTask(task: CreateTask): Observable<Task> {
    const mutation = gql`
      mutation {
        createTask(taskCreateDto: { name: "${task.name}" }) {
          id
          name
          status
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
          status
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

  processTask(taskId: string): Observable<void> {
    const mutation = gql`
      mutation {
        processTask(id: "${taskId}")
      }
    `;
    return this.apollo
      .mutate<{ processTask: boolean }>({ mutation })
      .pipe(map((result) => runCatching(result, () => {})));
  }

  subscribeToTaskUpdates(taskId: string): Observable<Task> {
    const query = gql`
      subscription {
        taskUpdates(id: "${taskId}") {
          id
          name
          status
        }
      }
    `;

    return new Observable((observer) => {
      const subscriptionHandle = this.subscriptionClient
        .subscribe<TaskUpdatesResponse>({ query })
        .subscribe({
          next: (result) =>
            observer.next(runCatching(result, (data) => data.taskUpdates)),
          error: (error) => observer.error(error),
          complete: () => observer.complete(),
        });

      return () => subscriptionHandle.unsubscribe();
    });
  }
}
