import { NgModule, ApplicationRef } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule, PreloadAllModules } from '@angular/router';
import { TranslateModule } from 'ng2-translate';
import { RacesService } from '../services/races.service';
import { EntriesService } from '../services/entries.service';
import { ClubsService } from '../services/clubs.service';

import { ENV_PROVIDERS } from './environment';
import { timingRouting } from './timing.routes';
import { TimingComponent } from './timing.component';
import { LandingComponent } from './landing';
import { NoContentComponent } from './no-content';
import { TimingEntryComponent } from './time_entry';

import { FooterComponent } from '../components/footer/footer.component';
import { TitlebarComponent } from '../components/titlebar/titlebar.component';
import { SidebarComponent } from '../components/sidebar/sidebar.component';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { Ng2PaginationModule } from 'ng2-pagination';

@NgModule({
  bootstrap: [ TimingComponent ],
  declarations: [
    TimingComponent,
    LandingComponent,
    NoContentComponent,
    FooterComponent,
    TitlebarComponent,
    SidebarComponent,
    TimingEntryComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    Ng2PaginationModule,
    NgbModule.forRoot(),
    TranslateModule.forRoot(),
    timingRouting
  ],
  providers: [
    ENV_PROVIDERS,
    RacesService,
    EntriesService,
    ClubsService,
    Title
  ]
})
export class TimingModule {
}

