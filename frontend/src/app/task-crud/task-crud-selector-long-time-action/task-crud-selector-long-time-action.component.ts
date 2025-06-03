import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';

export type LongTimeAction =
  | 'short-poll'
  | 'long-poll'
  | 'graphql-subscription'
  | 'webhook+sse'
  | 'websocket'
  | 'grpc';

@Component({
  selector: 'app-task-crud-selector-long-time-action',
  imports: [TranslatePipe],
  templateUrl: './task-crud-selector-long-time-action.component.html',
})
export class TaskCrudSelectorLongTimeActionComponent {
  @Input({ required: true }) longTimeAction!: LongTimeAction;
  @Output() longTimeActionChange = new EventEmitter<LongTimeAction>();
  actions: LongTimeAction[] = [
    'short-poll',
    'long-poll',
    'graphql-subscription',
    // 'webhook+sse',
    // 'websocket',
    // 'grpc',
  ];

  updateLongTimeAction(action: LongTimeAction) {
    this.longTimeActionChange.emit(action);
  }
}
