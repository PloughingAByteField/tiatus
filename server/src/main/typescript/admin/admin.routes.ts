import { Routes, RouterModule } from '@angular/router';
import { LandingComponent } from './landing';
import { ClubsComponent } from './clubs';
import { ConfigComponent } from './config';
import { DrawComponent } from './draw';
import { EntriesComponent } from './entries';
import { EventsComponent } from './events';
import { PositionsComponent } from './positions';
import { RacePositionsComponent } from './race-positions';
import { RacesComponent } from './races';
import { UsersComponent } from './users';
import { RacePositionTemplatesComponent } from './race-position-templates';
import { CreateEventComponent } from './events/create-event';
import { EventsTableComponent } from './events/events-table';
import { EditEventComponent } from './events/edit-event';
import { EntriesTableComponent } from './entries/entries-table';
import { CreateEntryComponent } from './entries/create-entry';
import { EditEntryComponent } from './entries/edit-entry';

import { NoContentComponent } from '../components/no-content/no-content.component';

export const adminRoutes: Routes = [
  { path: '',      component: LandingComponent, pathMatch: 'full' },
  { path: 'clubs',      component: ClubsComponent },
  { path: 'config',      component: ConfigComponent },
  { path: 'draw',      component: DrawComponent },
  { path: 'entries',      component: EntriesComponent,
    children: [
      { path: 'create', component: CreateEntryComponent, pathMatch: 'full' },
      { path: ':raceId', component: EntriesTableComponent },
      { path: 'edit/:entryId', component: EditEntryComponent }
    ]},
  { path: 'events',      component: EventsComponent,
    children: [
    { path: 'create', component: CreateEventComponent },
    { path: ':raceId', component: EventsTableComponent },
    { path: 'edit/:eventId', component: EditEventComponent }
    ]
  },
  { path: 'positions',      component: PositionsComponent },
  { path: 'race-positions',      component: RacePositionsComponent },
  { path: 'race-positions/:raceId',      component: RacePositionsComponent },
  { path: 'race-position-template/:templateId',      component: RacePositionTemplatesComponent },
  { path: 'races',      component: RacesComponent },
  { path: 'users',      component: UsersComponent },
  { path: '**',    component: NoContentComponent }
];
