import { NgModule, ApplicationRef } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule, PreloadAllModules } from '@angular/router';
import { TranslateModule } from 'ng2-translate';

import { RacesService } from '../races/races.service';
import { RacesHttpService } from '../races/races-http.service';
import { EntriesService } from '../entries/entries.service';
import { EntriesHttpService } from '../entries/entries-http.service';
import { ClubsService } from '../clubs/clubs.service';
import { ClubsHttpService } from '../clubs/clubs-http.service';
import { PositionsService } from '../http-services/positions.service';
import { ConfigService } from '../http-services/config.service';

import { ENV_PROVIDERS } from './environment';
import { timingRouting } from './timing.routes';
import { TimingComponent } from './timing.component';
import { PositionComponent } from './position';

import { TimingEntryComponent } from './time_entry';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { Ng2PaginationModule } from 'ng2-pagination';

import { FooterModule } from '../components/footer/footer.module';
import { SidebarModule } from '../components/sidebar/sidebar.module';
import { TitlebarModule } from '../components/titlebar/titlebar.module';
import { NoContentModule } from '../components/no-content/no-content.module';

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
    HttpModule,
    Ng2PaginationModule,
    FooterModule,
    SidebarModule,
    TitlebarModule,
    NoContentModule,
    NgbModule.forRoot(),
    TranslateModule.forRoot(),
    timingRouting
  ],
  providers: [
    ENV_PROVIDERS,
    RacesService,
    RacesHttpService,
    EntriesService,
    EntriesHttpService,
    ClubsService,
    ClubsHttpService,
    PositionsService,
    ConfigService,
    Title
  ]
})
export class TimingModule {
}
