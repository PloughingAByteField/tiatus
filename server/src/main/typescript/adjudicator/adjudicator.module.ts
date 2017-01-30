import { NgModule, ApplicationRef } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule, PreloadAllModules } from '@angular/router';
import { TranslateModule } from 'ng2-translate';
import { RacesService } from '../services/races.service';

import { ENV_PROVIDERS } from './environment';
import { adjudicatorRoutes } from './adjudicator.routes';
import { AdjudicatorComponent } from './adjudicator.component';
import { LandingComponent } from './landing';
import { NoContentComponent } from './no-content';
import { ResultsComponent } from './race_results';

import { FooterComponent } from '../components/footer/footer.component';
import { TitlebarComponent } from '../components/titlebar/titlebar.component';
import { SidebarComponent } from '../components/sidebar/sidebar.component';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  bootstrap: [ AdjudicatorComponent ],
  declarations: [
    AdjudicatorComponent,
    LandingComponent,
    NoContentComponent,
    FooterComponent,
    TitlebarComponent,
    SidebarComponent,
    ResultsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    NgbModule.forRoot(),
    TranslateModule.forRoot(),
    RouterModule.forRoot(
      adjudicatorRoutes, { useHash: false, preloadingStrategy: PreloadAllModules}
    )
  ],
  providers: [
    ENV_PROVIDERS,
    RacesService,
    Title
  ]
})
export class AdjudicatorModule {
}
