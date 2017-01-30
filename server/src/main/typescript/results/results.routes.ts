import { Routes, RouterModule } from '@angular/router';
import { LandingComponent } from './landing';
import { NoContentComponent } from './no-content';
import { RaceResultsComponent } from './race_results';

export const ROUTES: Routes = [
  { path: '',      component: LandingComponent },
  { path: 'race/:raceId',  component: RaceResultsComponent },
  { path: '**',    component: NoContentComponent }
];
