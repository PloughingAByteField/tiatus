import { ModuleWithProviders } from '@angular/core'
import { Routes, RouterModule, PreloadAllModules } from '@angular/router';
import { LandingComponent } from './landing';
import { NoContentComponent } from './no-content';
import { TimingComponent } from './time_entry';

const ROUTES: Routes = [
  { path: '',      component: LandingComponent },
  { path: 'race/:raceId',  component: TimingComponent },
  { path: '**',    component: NoContentComponent }
];

export const appRouting: ModuleWithProviders = RouterModule.forRoot(ROUTES, { preloadingStrategy: PreloadAllModules });