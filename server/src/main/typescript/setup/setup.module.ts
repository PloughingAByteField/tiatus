import { NgModule, ApplicationRef } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { RouterModule, PreloadAllModules } from '@angular/router';

import 'rxjs/add/operator/toPromise';

import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { SetupComponent } from './setup.component';
import { SetupService } from './setup.service';
import { SetupHttpService } from './setup-http.service';

export function HttpLoaderFactory(http: HttpClient) {
    return new TranslateHttpLoader(http);
}

export function createTranslateLoader(http: HttpClient) {
    return new TranslateHttpLoader(http, './i18n/', '.json');
}

@NgModule({
  bootstrap: [ SetupComponent ],
  declarations: [
    SetupComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    TranslateModule.forRoot({
        loader: {
            provide: TranslateLoader,
            useFactory: (createTranslateLoader),
            deps: [HttpClient]
        }
    }),
    RouterModule.forRoot([], { useHash: false, preloadingStrategy: PreloadAllModules})
  ],
  providers: [
    Title,
    SetupService,
    SetupHttpService
  ]
})
export class SetupModule {
}
