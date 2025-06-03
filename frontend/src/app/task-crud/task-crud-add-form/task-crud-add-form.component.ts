import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TranslatePipe } from '@ngx-translate/core';
import { ValidLengthPipe } from 'src/app/pipes/valid-length.pipe';
import { handle } from 'src/app/utils/handle';
import { CreateTask } from '../task.model';
import { TaskServiceInterface } from '../task.service.interface';

const INITIAL_TASK_STATE: CreateTask = { name: '' };

@Component({
  selector: 'app-task-crud-add-form',
  imports: [TranslatePipe, ValidLengthPipe, FormsModule],
  templateUrl: './task-crud-add-form.component.html',
})
export class TaskCrudAddFormComponent {
  @Input({ required: true }) taskService!: TaskServiceInterface;
  @Output() taskAdded = new EventEmitter<void>();

  newTask: CreateTask = INITIAL_TASK_STATE;

  addTaskClick() {
    if (this.newTask.name.trim()) {
      handle(this.taskService.addTask(this.newTask), 'adding', () =>
        this.taskAdded.emit()
      );
      this.newTask = INITIAL_TASK_STATE;
    }
  }
}
