import { Routes, RouterModule } from '@angular/router';
import { LandingComponent } from './landing';
import { NoContentComponent } from '../components/no-content/no-content.component';
import { DisqualificationComponent } from './disqualification';
import { EntriesComponent } from './entries';
import { PenaltiesComponent } from './penalties';
import { SwapEntriesComponent } from './swap-entries';

export const adjudicatorRoutes: Routes = [
  { path: '',      component: LandingComponent },
  { path: 'penalty/:entryId',  component: PenaltiesComponent },
  { path: 'disqualify/:entryId',  component: DisqualificationComponent },
  { path: 'race/:raceId',  component: EntriesComponent },
  { path: 'swap/:raceId',  component: SwapEntriesComponent },
  { path: '**',    component: NoContentComponent }
];
