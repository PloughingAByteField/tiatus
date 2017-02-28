import { Routes, RouterModule } from '@angular/router';
import { LandingComponent } from './landing';
import { NoContentComponent } from '../components/no-content/no-content.component';
import { EntriesComponent } from './entries';

export const adjudicatorRoutes: Routes = [
  { path: '',      component: LandingComponent },
  { path: 'race/:raceId',  component: EntriesComponent },
  { path: '**',    component: NoContentComponent }
];
