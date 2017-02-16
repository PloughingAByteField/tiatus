import { NgModule, ApplicationRef } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule, PreloadAllModules } from '@angular/router';
import { TranslateModule } from 'ng2-translate';
import { RacesService } from '../services/races.service';
import { EntriesService } from '../services/entries.service';
import { ClubsService } from '../services/clubs.service';
import { PositionsService } from '../services/positions.service';

import { ENV_PROVIDERS } from './environment';
import { timingRouting } from './timing.routes';
import { TimingComponent } from './timing.component';
import { LandingComponent } from './landing';

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
    LandingComponent,
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
    EntriesService,
    ClubsService,
    PositionsService,
    Title
  ]
})
export class TimingModule {
}
