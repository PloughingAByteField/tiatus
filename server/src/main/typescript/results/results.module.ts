import { NgModule, ApplicationRef } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { RouterModule, PreloadAllModules } from '@angular/router';

import 'rxjs/add/operator/toPromise';
import 'rxjs/add/observable/interval';

import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { NgxPaginationModule } from 'ngx-pagination';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { ResultsRacesService } from './races/races.service';
import { ResultsHttpRacesService } from './races/races-http.service';
import { RacesHttpService } from '../races/races-http.service';
import { EntriesService } from '../entries/entries.service';
import { EntriesHttpService } from '../entries/entries-http.service';
import { TimesService } from '../times/times.service';
import { TimesHttpService } from '../times/times-http.service';
import { ConfigService } from '../config/config.service';
import { ConfigHttpService } from '../config/config-http.service';
import { ResultsPositionsService } from './positions/positions.service';
import { ResultsHttpPositionsService } from './positions/positions-http.service';
import { PositionsHttpService } from '../positions/positions-http.service';
import { ResultsPenaltiesService } from './penalties/penalties.service';
import { ResultsHttpPenaltiesService } from './penalties/penalties-http.service';
import { PenaltiesHttpService } from '../penalties/penalties-http.service';
import { ResultsDisqualificationService } from './disqualification/disqualification.service';
import { ResultsHttpDisqualificationsService } from './disqualification/disqualification-http.service';
import { DisqualificationHttpService } from '../disqualification/disqualification-http.service';
import { ResultsClubsService } from './clubs/clubs.service';
import { ResultsHttpClubsService } from './clubs/clubs-http.service';
import { ClubsHttpService } from '../clubs/clubs-http.service';
import { ResultsEventsService } from './events/events.service';
import { ResultsHttpEventsService } from './events/events-http.service';
import { EventsHttpService } from '../events/events-http.service';
import { RaceEventsService } from '../race-events/race-events.service';
import { RaceEventsHttpService } from '../race-events/race-events-http.service';

import { resultsRoutes } from './results.routes';
import { ResultsComponent } from './results.component';
import { LandingComponent } from './landing';

import { RaceResultsComponent } from './race_results';
import { RaceResultsTableComponent } from './race_results/results-table/results-table.component';

import { FooterModule } from '../components/footer/footer.module';
import { SidebarModule } from '../components/sidebar/sidebar.module';
import { TitlebarModule } from '../components/titlebar/titlebar.module';
import { NoContentModule } from '../components/no-content/no-content.module';

export function HttpLoaderFactory(http: HttpClient) {
    return new TranslateHttpLoader(http);
}

export function createTranslateLoader(http: HttpClient) {
    return new TranslateHttpLoader(http, 'results/i18n/', '.json');
}

@NgModule({
  bootstrap: [ ResultsComponent ],
  declarations: [
    ResultsComponent,
    LandingComponent,
    RaceResultsComponent,
    RaceResultsTableComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    FooterModule,
    SidebarModule,
    TitlebarModule,
    NoContentModule,
    NgxPaginationModule,
    NgbModule,
    TranslateModule.forRoot({
        loader: {
            provide: TranslateLoader,
            useFactory: (createTranslateLoader),
            deps: [HttpClient]
        }
    }),
    RouterModule.forRoot(resultsRoutes, { useHash: false, preloadingStrategy: PreloadAllModules, relativeLinkResolution: 'legacy' })
  ],
  providers: [
    ResultsRacesService,
    ResultsHttpRacesService,
    RacesHttpService,
    EntriesService,
    EntriesHttpService,
    ResultsPositionsService,
    ResultsHttpPositionsService,
    PositionsHttpService,
    TimesService,
    TimesHttpService,
    ConfigService,
    ConfigHttpService,
    ResultsPenaltiesService,
    ResultsHttpPenaltiesService,
    PenaltiesHttpService,
    ResultsDisqualificationService,
    ResultsHttpDisqualificationsService,
    DisqualificationHttpService,
    ResultsClubsService,
    ResultsHttpClubsService,
    ClubsHttpService,
    ResultsEventsService,
    ResultsHttpEventsService,
    EventsHttpService,
    RaceEventsService,
    RaceEventsHttpService,
    Title
  ]
})
export class ResultsModule {
}
