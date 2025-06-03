import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TranslatePipe } from '@ngx-translate/core';
import { TaskCrudAddFormComponent } from './task-crud-add-form/task-crud-add-form.component';
import { TaskCrudCardComponent } from './task-crud-card/task-crud-card.component';
import { TaskCrudLinksComponent } from './task-crud-links/task-crud-links.component';
import {
  CrudAction,
  TaskCrudSelectorCrudActionComponent,
} from './task-crud-selector-crud-action/task-crud-selector-crud-action.component';
import {
  LongTimeAction,
  TaskCrudSelectorLongTimeActionComponent,
} from './task-crud-selector-long-time-action/task-crud-selector-long-time-action.component';
import { TaskGraphQLService } from './task-graphql.service';
import { Task } from './task.model';
import { TaskRestService } from './task.service';
import { TaskServiceInterface } from './task.service.interface';

@Component({
  selector: 'app-task-crud',
  imports: [
    TranslatePipe,
    TaskCrudSelectorCrudActionComponent,
    TaskCrudSelectorLongTimeActionComponent,
    TaskCrudLinksComponent,
    NgIf,
    NgFor,
    FormsModule,
    TaskCrudAddFormComponent,
    TaskCrudCardComponent,
  ],
  templateUrl: './task-crud.component.html',
})
export class TaskCrudComponent implements OnInit {
  tasks: Task[] = [];
  useGraphQL: CrudAction = 'rest-api';
  longTimeAction: LongTimeAction = 'short-poll';

  constructor(
    public readonly restService: TaskRestService,
    public readonly graphQLService: TaskGraphQLService
  ) {}

  get taskService(): TaskServiceInterface {
    return this.useGraphQL ? this.graphQLService : this.restService;
  }

  ngOnInit() {
    this.loadTasks();
  }

  loadTasks() {
    this.taskService.getTasks().subscribe((data) => (this.tasks = data));
  }
}
