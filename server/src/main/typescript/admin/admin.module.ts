import { NgModule, ApplicationRef } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpModule, Http } from '@angular/http';
import { RouterModule, PreloadAllModules } from '@angular/router';

import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { ENV_PROVIDERS } from './environment';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { Ng2PaginationModule } from 'ng2-pagination';

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

import { SelectedRaceService } from './races/selected-race.service';
import { AdminRacesService } from './races/races.service';
import { AdminRacesHttpService } from './races/races-http.service';
import { RacesHttpService } from '../races/races-http.service';

import { RacePositionsService } from './race-positions/race-positions.service';
import { RacePositionsHttpService } from './race-positions/race-positions-http.service';

import { RacePositionTemplatesService } from
  './race-position-templates/race-position-templates.service';
import { RacePositionTemplatesHttpService } from
  './race-position-templates/race-position-templates-http.service';

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
import { CreateEntryComponent } from './entries/create-entry';
import { EditEntryComponent } from './entries/edit-entry';

import { AdminEventsService } from './events/events.service';
import { AdminEventsHttpService } from './events/events-http.service';
import { EventsHttpService } from '../events/events-http.service';

import { AdminRolesService } from './users/roles.service';
import { AdminRolesHttpService } from './users/roles-http.service';
import { AdminUsersService } from './users/users.service';
import { AdminUsersHttpService } from './users/users-http.service';

import { RacePositionTemplatesComponent } from './race-position-templates';
import { CreateEventComponent } from './events/create-event';
import { EditEventComponent } from './events/edit-event';
import { AdminRaceEventsHttpService } from './events/race-events-http.service';
import { AdminRaceEventsService } from './events/race-events.service';
import { AdminUnassignedEventsHttpService } from './events/unassigned-events-http.service';
import { AdminUnassignedEventsService } from './events/unassigned-events.service';
import { RaceEventsHttpService } from '../race-events/race-events-http.service';
import { RaceEventsService } from '../race-events/race-events.service';

import { FooterModule } from '../components/footer/footer.module';
import { SidebarModule } from '../components/sidebar/sidebar.module';
import { TitlebarModule } from '../components/titlebar/titlebar.module';
import { NoContentModule } from '../components/no-content/no-content.module';

export function HttpLoaderFactory(http: Http) {
    return new TranslateHttpLoader(http);
}

export function createTranslateLoader(http: Http) {
    return new TranslateHttpLoader(http, './i18n/', '.json');
}

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
    RacePositionsComponent,
    RacePositionTemplatesComponent,
    CreateEventComponent,
    EditEventComponent,
    CreateEntryComponent,
    EditEntryComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    Ng2PaginationModule,
    FooterModule,
    SidebarModule,
    TitlebarModule,
    NoContentModule,
    NgbModule.forRoot(),
    TranslateModule.forRoot({
        loader: {
            provide: TranslateLoader,
            useFactory: (createTranslateLoader),
            deps: [Http]
        }
    }),
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
    RacePositionTemplatesService,
    RacePositionTemplatesHttpService,
    RaceEventsHttpService,
    RaceEventsService,
    AdminRaceEventsService,
    AdminRaceEventsHttpService,
    AdminUnassignedEventsHttpService,
    AdminUnassignedEventsService,
    AdminRolesHttpService,
    AdminRolesService,
    SelectedRaceService,
    Title
  ]
})
export class AdminModule {
}
