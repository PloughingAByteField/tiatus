import { NgModule, ApplicationRef } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule, PreloadAllModules } from '@angular/router';
import { TranslateModule } from 'ng2-translate';

import { ENV_PROVIDERS } from './environment';

import { adminRoutes } from './admin.routes';
import { AdminComponent } from './admin.component';
import { LandingComponent } from './landing';
import { MenuComponent } from './menu';

import { ClubsComponent } from './clubs';
import { ConfigComponent } from './config';
import { DrawComponent } from './draw';
import { EntriesComponent } from './entries';
import { EventsComponent } from './events';
import { PositionsComponent } from './positions';
import { RacePositionsComponent } from './race-positions';
import { RacesComponent } from './races';
import { UsersComponent } from './users';

import { AdminPositionsService } from './positions/positions.service';
import { AdminPositionsHttpService } from './positions/positions-http.service';
import { PositionsHttpService } from '../positions/positions-http.service';

import { AdminRacesService } from './races/races.service';
import { AdminRacesHttpService } from './races/races-http.service';
import { RacesHttpService } from '../races/races-http.service';

import { RacePositionsService } from './race-positions/race-positions.service';
import { RacePositionsHttpService } from './race-positions/race-positions-http.service';

import { AdminConfigService } from './config/config.service';
import { AdminConfigHttpService } from './config/config-http.service';
import { ConfigHttpService } from '../config/config-http.service';

import { AdminClubsService } from './clubs/clubs.service';
import { AdminClubsHttpService } from './clubs/clubs-http.service';
import { ClubsHttpService } from '../clubs/clubs-http.service';

import { AdminDrawService } from './draw/draw.service';
import { AdminDrawHttpService } from './draw/draw-http.service';

import { AdminEntriesService } from './entries/entries.service';
import { AdminEntriesHttpService } from './entries/entries-http.service';
import { EntriesHttpService } from '../entries/entries-http.service';

import { AdminEventsService } from './events/events.service';
import { AdminEventsHttpService } from './events/events-http.service';
import { EventsHttpService } from '../events/events-http.service';

import { AdminUsersService } from './users/users.service';
import { AdminUsersHttpService } from './users/users-http.service';

import { FooterModule } from '../components/footer/footer.module';
import { SidebarModule } from '../components/sidebar/sidebar.module';
import { TitlebarModule } from '../components/titlebar/titlebar.module';
import { NoContentModule } from '../components/no-content/no-content.module';

@NgModule({
  bootstrap: [ AdminComponent ],
  declarations: [
    AdminComponent,
    LandingComponent,
    MenuComponent,
    ClubsComponent,
    ConfigComponent,
    DrawComponent,
    EntriesComponent,
    EventsComponent,
    PositionsComponent,
    RacesComponent,
    UsersComponent,
    RacePositionsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    FooterModule,
    SidebarModule,
    TitlebarModule,
    NoContentModule,
    TranslateModule.forRoot(),
    RouterModule.forRoot(
      adminRoutes, { useHash: false, preloadingStrategy: PreloadAllModules}
    )
  ],
  providers: [
    ENV_PROVIDERS,
    AdminRacesService,
    AdminRacesHttpService,
    RacesHttpService,
    AdminConfigService,
    AdminConfigHttpService,
    ConfigHttpService,
    AdminPositionsService,
    AdminPositionsHttpService,
    PositionsHttpService,
    AdminClubsService,
    AdminClubsHttpService,
    ClubsHttpService,
    AdminDrawService,
    AdminDrawHttpService,
    AdminEntriesService,
    AdminEntriesHttpService,
    EntriesHttpService,
    AdminEventsService,
    AdminEventsHttpService,
    EventsHttpService,
    AdminUsersService,
    AdminUsersHttpService,
    RacePositionsService,
    RacePositionsHttpService,
    Title
  ]
})
export class AdminModule {
}
