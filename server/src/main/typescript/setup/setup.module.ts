import { NgModule, ApplicationRef } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule, Http } from '@angular/http';
import { RouterModule, PreloadAllModules } from '@angular/router';

import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { ENV_PROVIDERS } from './environment';
import { SetupComponent } from './setup.component';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { FooterModule } from '../components/footer/footer.module';

export function HttpLoaderFactory(http: Http) {
    return new TranslateHttpLoader(http);
}

export function createTranslateLoader(http: Http) {
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
    HttpModule,
    NgbModule.forRoot(),
    TranslateModule.forRoot({
        loader: {
            provide: TranslateLoader,
            useFactory: (createTranslateLoader),
            deps: [Http]
        }
    })
  ],
  providers: [
    ENV_PROVIDERS,
    Title
  ]
})
export class SetupModule {
}
