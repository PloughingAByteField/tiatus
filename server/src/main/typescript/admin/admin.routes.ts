import { Routes, RouterModule } from '@angular/router';
import { LandingComponent } from './landing';
import { ClubsComponent } from './clubs';
import { ConfigComponent } from './config';
import { DrawComponent } from './draw';
import { EntriesComponent } from './entries';
import { EventsComponent } from './events';
import { PositionsComponent } from './positions';
import { RacesComponent } from './races';
import { UsersComponent } from './users';
import { NoContentComponent } from '../components/no-content/no-content.component';

export const adminRoutes: Routes = [
  { path: '',      component: LandingComponent, pathMatch: 'full' },
  { path: 'clubs',      component: ClubsComponent },
  { path: 'config',      component: ConfigComponent },
  { path: 'draw',      component: DrawComponent },
  { path: 'entries',      component: EntriesComponent },
  { path: 'events',      component: EventsComponent },
  { path: 'positions',      component: PositionsComponent },
  { path: 'races',      component: RacesComponent },
  { path: 'users',      component: UsersComponent },
  { path: '**',    component: NoContentComponent }
];
