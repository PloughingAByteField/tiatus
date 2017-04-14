import { Routes, RouterModule } from '@angular/router';
import { LandingComponent } from './landing';
import { NoContentComponent } from '../components/no-content/no-content.component';
import { RaceResultsComponent } from './race_results';
import { RaceResultsTableComponent } from './race_results/results-table/results-table.component';

export const resultsRoutes: Routes = [
  { path: '',      component: LandingComponent },
  { path: 'race/:raceId',  component: RaceResultsComponent ,
    children: [
      // { path: '', redirectTo: 'start/finish', pathMatch: 'full' },
      { path: ':from/:to', component: RaceResultsTableComponent }
    ]
  },
  { path: '**',    component: NoContentComponent }
];
