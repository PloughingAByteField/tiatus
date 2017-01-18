import { Routes, RouterModule } from '@angular/router';
import { LandingComponent } from './landing';
import { NoContentComponent } from './no-content';
import { TimingComponent } from './time_entry';

export const ROUTES: Routes = [
  { path: '',      component: LandingComponent },
  { path: 'race/:raceId',  component: TimingComponent },
  { path: '**',    component: NoContentComponent }
];
