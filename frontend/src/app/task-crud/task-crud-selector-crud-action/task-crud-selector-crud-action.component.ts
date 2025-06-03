import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';

export type CrudAction = 'rest-api' | 'graphql';

@Component({
  selector: 'app-task-crud-selector-crud-action',
  imports: [TranslatePipe],
  templateUrl: './task-crud-selector-crud-action.component.html',
})
export class TaskCrudSelectorCrudActionComponent {
  @Input({ required: true }) crudAction!: CrudAction;
  @Output() crudActionChange = new EventEmitter<CrudAction>();

  readonly actions: CrudAction[] = ['rest-api', 'graphql'];
}
