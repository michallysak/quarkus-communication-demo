import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TranslatePipe } from '@ngx-translate/core';
import { interval, switchMap, takeWhile } from 'rxjs';
import { handle } from 'src/app/utils/handle';
import { ValidLengthPipe } from '../../pipes/valid-length.pipe';
import { LongTimeAction } from '../task-crud-selector-long-time-action/task-crud-selector-long-time-action.component';
import { TaskGraphQLService } from '../task-graphql.service';
import { TaskStatusBadgeComponent } from '../task-status-badge/task-status-badge.component';
import { Task, TaskStatus } from '../task.model';
import { TaskRestService } from '../task.service';
import { TaskServiceInterface } from '../task.service.interface';


@Component({
  selector: 'app-task-crud-card',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TranslatePipe,
    ValidLengthPipe,
    TaskStatusBadgeComponent,
  ],
  templateUrl: './task-crud-card.component.html',
})
export class TaskCrudCardComponent {
  @Input({ required: true }) task!: Task;
  @Input({ required: true }) longTimeAction!: LongTimeAction;
  @Input({ required: true }) taskService!: TaskServiceInterface;
  @Input({ required: true }) restService!: TaskRestService;
  @Input({ required: true }) graphQLService!: TaskGraphQLService;
  @Output() updated = new EventEmitter<void>();
  @Output() deleted = new EventEmitter<void>();

  editTask: Task | null = null;

  startEdit() {
    this.editTask = { ...this.task };
  }

  updateTask() {
    if (!this.editTask) {
      return;
    }

    handle(this.taskService.updateTask(this.editTask), 'updating', () =>
      this.updated.emit()
    );
  }

  deleteTask() {
    handle(this.taskService.deleteTask(this.task.id), 'deleting', () =>
      this.deleted.emit()
    );
  }

  cancelEdit() {
    this.editTask = null;
  }

  processTask(taskId: string) {
    this.taskService
      .processTask(taskId)
      .subscribe(() => this.handleLongTimeAction(taskId));
  }

  private handleLongTimeAction(taskId: string) {
    switch (this.longTimeAction) {
      case 'short-poll':
        this.pollShort(taskId);
        break;
      case 'long-poll':
        this.pollLong(taskId);
        break;
      case 'graphql-subscription':
        this.subscribeGraphql(taskId);
        break;
      case 'webhook+sse':
        break;
      case 'websocket':
        break;
      case 'grpc':
        break;
    }
  }

  private pollShort(taskId: string) {
    interval(500)
      .pipe(
        switchMap(() => this.taskService.getTask(taskId)),
        takeWhile(({ status }) => status !== 'COMPLETED', true)
      )
      .subscribe(({ status }) => {
        this.updateTaskStatus(status);
      });
  }

  private pollLong(taskId: string) {
    this.updateTaskStatus('IN_PROGRESS');

    this.restService.longPoll(taskId).subscribe(({ status }) => {
      this.updateTaskStatus(status);
      if (status !== 'COMPLETED') {
        this.pollLong(taskId);
      }
    });
  }

  private subscribeGraphql(taskId: string) {
    this.graphQLService
      .subscribeToTaskUpdates(taskId)
      .pipe(takeWhile((task: Task) => task.status !== 'COMPLETED', true))
      .subscribe(({ status }) => this.updateTaskStatus(status));
  }

  private updateTaskStatus(status: TaskStatus) {
    this.task = { ...this.task, status };
  }
}
