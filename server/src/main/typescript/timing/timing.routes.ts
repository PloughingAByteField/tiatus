import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule, PreloadAllModules } from '@angular/router';
import { LandingComponent } from './landing';
import { NoContentComponent } from './no-content';
import { TimingEntryComponent } from './time_entry';

const timingRoutes: Routes = [
  { path: '',      component: LandingComponent },
  { path: 'race/:raceId',  component: TimingEntryComponent },
  { path: '**',    component: NoContentComponent }
];

export const timingRouting: ModuleWithProviders = RouterModule.forRoot(
    timingRoutes, { preloadingStrategy: PreloadAllModules }
);
