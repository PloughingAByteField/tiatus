import { NgModule, ApplicationRef } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { RouterModule, PreloadAllModules } from '@angular/router';

import 'rxjs/add/operator/toPromise';

import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { NgIdleKeepaliveModule } from '@ng-idle/keepalive';

import { RacesService } from '../races/races.service';
import { RacesHttpService } from '../races/races-http.service';
import { TimesService } from '../times/times.service';
import { TimesHttpService } from '../times/times-http.service';
import { TimingTimesService } from './times/times.service';
import { TimingTimesHttpService } from './times/times-http.service';
import { EntriesService } from '../entries/entries.service';
import { EntriesHttpService } from '../entries/entries-http.service';
import { ClubsService } from '../clubs/clubs.service';
import { ClubsHttpService } from '../clubs/clubs-http.service';
import { PositionsService } from '../positions/positions.service';
import { PositionsHttpService } from '../positions/positions-http.service';
import { ConfigService } from '../config/config.service';
import { ConfigHttpService } from '../config/config-http.service';
import { DisqualificationService } from '../disqualification/disqualification.service';
import { DisqualificationHttpService } from '../disqualification/disqualification-http.service';
import { PenaltiesService } from '../penalties/penalties.service';
import { PenaltiesHttpService } from '../penalties/penalties-http.service';
import { EventsService } from '../events/events.service';
import { EventsHttpService } from '../events/events-http.service';
import { WebSocketWSService } from '../websocket/websocket-ws-service';
import { TimingWebSocketService } from './websocket/websocket-service';

import { timingRouting } from './timing.routes';
import { TimingComponent } from './timing.component';
import { PositionComponent } from './position';

import { TimingEntryComponent } from './time_entry';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { NgxPaginationModule } from 'ngx-pagination';

import { FooterModule } from '../components/footer/footer.module';
import { SidebarModule } from '../components/sidebar/sidebar.module';
import { TitlebarModule } from '../components/titlebar/titlebar.module';
import { TimeEntryModule } from '../components/time-entry/time-entry.module';
import { MessagingModule } from '../components/messaging/messaging.module';
import { NoContentModule } from '../components/no-content/no-content.module';

export function HttpLoaderFactory(http: HttpClient) {
    return new TranslateHttpLoader(http);
}

export function createTranslateLoader(http: HttpClient) {
    return new TranslateHttpLoader(http, 'timing/i18n/', '.json');
}

@NgModule({
  bootstrap: [ TimingComponent ],
  declarations: [
    TimingComponent,
    PositionComponent,
    TimingEntryComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    NgxPaginationModule,
    FooterModule,
    SidebarModule,
    TitlebarModule,
    TimeEntryModule,
    MessagingModule,
    NoContentModule,
    NgbModule,
    NgIdleKeepaliveModule.forRoot(),
    TranslateModule.forRoot({
        loader: {
            provide: TranslateLoader,
            useFactory: (createTranslateLoader),
            deps: [HttpClient]
        }
    }),
    timingRouting
  ],
  providers: [
    RacesService,
    RacesHttpService,
    TimesService,
    TimingTimesService,
    TimingTimesHttpService,
    TimesHttpService,
    EntriesService,
    EntriesHttpService,
    ClubsService,
    ClubsHttpService,
    PositionsService,
    PositionsHttpService,
    ConfigService,
    ConfigHttpService,
    EventsService,
    EventsHttpService,
    DisqualificationService,
    DisqualificationHttpService,
    PenaltiesService,
    PenaltiesHttpService,
    WebSocketWSService,
    TimingWebSocketService,
    Title
  ]
})
export class TimingModule {
}
