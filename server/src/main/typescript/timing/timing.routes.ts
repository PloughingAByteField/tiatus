import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule, PreloadAllModules } from '@angular/router';
import { PositionComponent } from './position';
import { NoContentComponent } from '../components/no-content/no-content.component';
import { TimingEntryComponent } from './time_entry';

const timingRoutes: Routes = [
  { path: '',      component: PositionComponent },
  { path: 'race/:raceId',  component: TimingEntryComponent },
  { path: '**',    component: NoContentComponent }
];

export const timingRouting: ModuleWithProviders<any> = RouterModule.forRoot(
    timingRoutes, { preloadingStrategy: PreloadAllModules, relativeLinkResolution: 'legacy' }
);
