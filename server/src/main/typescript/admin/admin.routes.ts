import { Routes, RouterModule } from '@angular/router';
import { LandingComponent } from './landing';
import { NoContentComponent } from '../components/no-content/no-content.component';

export const adminRoutes: Routes = [
  { path: '',      component: LandingComponent },
  { path: '**',    component: NoContentComponent }
];
