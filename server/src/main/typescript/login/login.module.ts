import { NgModule, ApplicationRef } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule, Http } from '@angular/http';
import { RouterModule, PreloadAllModules } from '@angular/router';

import 'rxjs/add/operator/toPromise';

import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { ENV_PROVIDERS } from './environment';
import { LoginComponent } from './login.component';
import { LoginService } from './login.service';
import { LoginHttpService } from './login-http.service';

export function HttpLoaderFactory(http: Http) {
    return new TranslateHttpLoader(http);
}

export function createTranslateLoader(http: Http) {
    return new TranslateHttpLoader(http, './i18n/', '.json');
}

@NgModule({
  bootstrap: [ LoginComponent ],
  declarations: [
    LoginComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    TranslateModule.forRoot({
        loader: {
            provide: TranslateLoader,
            useFactory: (createTranslateLoader),
            deps: [Http]
        }
    }),
    RouterModule.forRoot([], { useHash: false, preloadingStrategy: PreloadAllModules})
  ],
  providers: [
    ENV_PROVIDERS,
    Title,
    LoginService,
    LoginHttpService
  ]
})
export class LoginModule {
}
