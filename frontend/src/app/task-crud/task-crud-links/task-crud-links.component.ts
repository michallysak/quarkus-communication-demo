import { NgFor } from '@angular/common';
import { Component } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';

const LINKS = [
  {
    url: 'http://localhost:8080/q/swagger-ui',
    label: 'swagger-ui',
  },
  {
    url: 'http://localhost:8080/q/openapi',
    label: 'openapi.yaml',
  },
  {
    url: 'http://localhost:8080/q/graphql-ui',
    label: 'graphql-ui',
  },
  {
    url: 'http://localhost:8080/graphql/schema.graphql',
    label: 'schema.graphql',
  },
];

@Component({
  selector: 'app-task-crud-links',
  imports: [TranslatePipe, NgFor],
  templateUrl: './task-crud-links.component.html',
})
export class TaskCrudLinksComponent {
  links = LINKS;
}
