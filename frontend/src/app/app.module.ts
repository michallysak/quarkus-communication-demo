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

export function HttpLoaderFactory(http: HttpClient): TranslateHttpLoader {
  return new TranslateHttpLoader(http, '/assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    TaskCrudComponent,
    ValidLengthPipe,
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
