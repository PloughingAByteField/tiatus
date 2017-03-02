import { Routes, RouterModule } from '@angular/router';
import { LandingComponent } from './landing';
import { NoContentComponent } from '../components/no-content/no-content.component';
import { EntriesComponent } from './entries';
import { PenaltiesComponent } from './penalties';

export const adjudicatorRoutes: Routes = [
  { path: '',      component: LandingComponent },
  { path: 'penalty/:entryId',  component: PenaltiesComponent },
  { path: 'race/:raceId',  component: EntriesComponent },
  { path: '**',    component: NoContentComponent }
];
