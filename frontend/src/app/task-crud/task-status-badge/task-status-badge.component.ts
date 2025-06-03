import { Component, Input } from '@angular/core';
import { TaskStatus } from '../task.model';

@Component({
  selector: 'app-task-status-badge',
  imports: [],
  templateUrl: './task-status-badge.component.html',
})
export class TaskStatusBadgeComponent {
  @Input({ required: true }) status!: TaskStatus;
}
