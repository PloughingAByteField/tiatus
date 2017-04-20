import { NgModule, ApplicationRef } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule, Http } from '@angular/http';
import { RouterModule, PreloadAllModules } from '@angular/router';

import 'rxjs/add/operator/toPromise';

import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { PenaltiesService } from '../penalties/penalties.service';
import { DisqualificationService } from '../disqualification/disqualification.service';

import { RacesService } from '../races/races.service';
import { RacesHttpService } from '../races/races-http.service';
import { AdjudicatorRacesService } from './races/races.service';
import { AdjudicatorRacesHttpService } from './races/races-http.service';
import { EntriesService } from '../entries/entries.service';
import { EntriesHttpService } from '../entries/entries-http.service';
import { TimesFullService } from './times/times.service';
import { EntryTimesFullService } from './times/entry-times.service';
import { TimesService } from '../times/times.service';
import { TimesHttpService } from '../times/times-http.service';
import { TimesFullHttpService } from './times/times-full-http.service';
import { ConfigService } from '../config/config.service';
import { ConfigHttpService } from '../config/config-http.service';
import { PenaltiesHttpService } from '../penalties/penalties-http.service';
import { DisqualificationHttpService } from '../disqualification/disqualification-http.service';
import { PositionsService } from '../positions/positions.service';
import { PositionsHttpService } from '../positions/positions-http.service';

import { AdjudicatorHttpDisqualificationsService }
    from './disqualification/disqualification-http.service';
import { AdjudicatorDisqualificationService } from './disqualification/disqualification.service';
import { AdjudicatorHttpPenaltiesService } from './penalties/penalties-http.service';
import { AdjudicatorPenaltiesService } from './penalties/penalties.service';
import { ClubsService } from '../clubs/clubs.service';
import { ClubsHttpService } from '../clubs/clubs-http.service';
import { EventsService } from '../events/events.service';
import { EventsHttpService } from '../events/events-http.service';

import { ENV_PROVIDERS } from './environment';
import { adjudicatorRoutes } from './adjudicator.routes';
import { AdjudicatorComponent } from './adjudicator.component';
import { LandingComponent } from './landing';
import { DisqualificationComponent } from './disqualification';
import { EntriesComponent } from './entries';
import { PenaltiesComponent } from './penalties';
import { SwapEntriesComponent } from './swap-entries';
import { WebSocketWSService } from '../websocket/websocket-ws-service';
import { WebSocketService } from '../websocket/websocket-service';

import { Ng2PaginationModule } from 'ng2-pagination';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { FooterModule } from '../components/footer/footer.module';
import { SidebarModule } from '../components/sidebar/sidebar.module';
import { TimeEntryModule } from '../components/time-entry/time-entry.module';
import { TitlebarModule } from '../components/titlebar/titlebar.module';
import { NoContentModule } from '../components/no-content/no-content.module';

export function HttpLoaderFactory(http: Http) {
    return new TranslateHttpLoader(http);
}

export function createTranslateLoader(http: Http) {
    return new TranslateHttpLoader(http, './i18n/', '.json');
}

@NgModule({
  bootstrap: [ AdjudicatorComponent ],
  declarations: [
    AdjudicatorComponent,
    LandingComponent,
    PenaltiesComponent,
    EntriesComponent,
    DisqualificationComponent,
    SwapEntriesComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    FooterModule,
    SidebarModule,
    TimeEntryModule,
    TitlebarModule,
    NoContentModule,
    Ng2PaginationModule,
    NgbModule.forRoot(),
    TranslateModule.forRoot({
        loader: {
            provide: TranslateLoader,
            useFactory: (createTranslateLoader),
            deps: [Http]
        }
    }),
    RouterModule.forRoot(
      adjudicatorRoutes, { useHash: false, preloadingStrategy: PreloadAllModules}
    )
  ],
  providers: [
    ENV_PROVIDERS,
    RacesService,
    RacesHttpService,
    AdjudicatorRacesService,
    AdjudicatorRacesHttpService,
    EntriesService,
    EntriesHttpService,
    EntryTimesFullService,
    TimesService,
    TimesFullService,
    TimesHttpService,
    TimesFullHttpService,
    ConfigService,
    ConfigHttpService,
    PenaltiesHttpService,
    PenaltiesService,
    PositionsService,
    PositionsHttpService,
    DisqualificationHttpService,
    DisqualificationService,
    AdjudicatorHttpDisqualificationsService,
    AdjudicatorDisqualificationService,
    AdjudicatorHttpPenaltiesService,
    AdjudicatorPenaltiesService,
    ClubsService,
    ClubsHttpService,
    EventsService,
    EventsHttpService,
    WebSocketWSService,
    WebSocketService,
    Title
  ]
})
export class AdjudicatorModule {
}
