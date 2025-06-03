import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TaskCrudComponent } from './task-crud/task-crud.component';
import { FormsModule } from '@angular/forms';
import { provideHttpClient, HttpClient } from '@angular/common/http';
import { ValidLengthPipe } from './pipes/valid-length.pipe';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TaskRestService } from './task-crud/task.service';
import { TaskGraphQLService } from './task-crud/task-graphql.service';
import { graphqlProvider } from 'src/app/graphql/graphql.provider';
import { TaskStatusBadgeComponent } from "./task-crud/task-status-badge/task-status-badge.component";
import { TaskCrudSelectorCrudActionComponent } from './task-crud/task-crud-selector-crud-action/task-crud-selector-crud-action.component';
import { TaskCrudSelectorLongTimeActionComponent } from './task-crud/task-crud-selector-long-time-action/task-crud-selector-long-time-action.component';
import { TaskCrudLinksComponent } from './task-crud/task-crud-links/task-crud-links.component';

export function HttpLoaderFactory(http: HttpClient): TranslateHttpLoader {
  return new TranslateHttpLoader(http, '/assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    TranslateModule.forRoot({
        defaultLanguage: 'en',
        loader: {
            provide: TranslateLoader,
            useFactory: HttpLoaderFactory,
            deps: [HttpClient]
        }
    }),
    TaskStatusBadgeComponent,
    TaskCrudSelectorCrudActionComponent,
    TaskCrudSelectorLongTimeActionComponent,
    TaskCrudComponent,
    ValidLengthPipe,
    TaskCrudLinksComponent
],
  providers: [
    graphqlProvider,
    TaskRestService,
    TaskGraphQLService,
    provideHttpClient()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
