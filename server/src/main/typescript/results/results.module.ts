import { NgModule, ApplicationRef } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule, PreloadAllModules } from '@angular/router';
import { TranslateModule } from 'ng2-translate';
import { RacesService } from '../services/races.service';
import { EntriesService } from '../services/entries.service';
import { TimesService } from './services/times.service';

import { ENV_PROVIDERS } from './environment';
import { resultsRoutes } from './results.routes';
import { ResultsComponent } from './results.component';
import { LandingComponent } from './landing';

import { RaceResultsComponent } from './race_results';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { FooterModule } from '../components/footer/footer.module';
import { SidebarModule } from '../components/sidebar/sidebar.module';
import { TitlebarModule } from '../components/titlebar/titlebar.module';
import { NoContentModule } from '../components/no-content/no-content.module';

@NgModule({
  bootstrap: [ ResultsComponent ],
  declarations: [
    ResultsComponent,
    LandingComponent,
    RaceResultsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    FooterModule,
    SidebarModule,
    TitlebarModule,
    NoContentModule,
    NgbModule.forRoot(),
    TranslateModule.forRoot(),
    RouterModule.forRoot(resultsRoutes, { useHash: false, preloadingStrategy: PreloadAllModules })
  ],
  providers: [
    ENV_PROVIDERS,
    RacesService,
    EntriesService,
    TimesService,
    Title
  ]
})
export class ResultsModule {
}
