import { Routes, RouterModule } from '@angular/router';
import { LandingComponent } from './landing';
import { NoContentComponent } from '../components/no-content/no-content.component';
import { ResultsComponent } from './race_results';

export const adjudicatorRoutes: Routes = [
  { path: '',      component: LandingComponent },
  { path: 'race/:raceId',  component: ResultsComponent },
  { path: '**',    component: NoContentComponent }
];
