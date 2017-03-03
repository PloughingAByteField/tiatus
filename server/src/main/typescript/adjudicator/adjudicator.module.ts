import { NgModule, ApplicationRef } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule, PreloadAllModules } from '@angular/router';
import { TranslateModule } from 'ng2-translate';

import { PenaltiesService } from '../services/penalties.service';
import { DisqualificationService } from '../services/disqualification.service';

import { RacesService } from '../races/races.service';
import { RacesHttpService } from '../races/races-http.service';
import { EntriesService } from '../entries/entries.service';
import { EntriesHttpService } from '../entries/entries-http.service';
import { TimesService } from '../times/times.service';
import { TimesHttpService } from '../times/times-http.service';
import { ConfigService } from '../http-services/config.service';
import { PenaltiesHttpService } from '../http-services/penalties.service';
import { DisqualificationHttpService } from '../http-services/disqualification.service';

import { AdjudicatorHttpDisqualificationsService }
    from './disqualification/disqualification-http.service';
import { AdjudicatorDisqualificationService } from './disqualification/disqualification.service';
import { AdjudicatorHttpPenaltiesService } from './http-services/penalties.service';
import { AdjudicatorPenaltiesService } from './services/penalties.service';

import { ENV_PROVIDERS } from './environment';
import { adjudicatorRoutes } from './adjudicator.routes';
import { AdjudicatorComponent } from './adjudicator.component';
import { LandingComponent } from './landing';
import { DisqualificationComponent } from './disqualification';
import { EntriesComponent } from './entries';
import { PenaltiesComponent } from './penalties';
import { SwapEntriesComponent } from './swap-entries';

import { Ng2PaginationModule } from 'ng2-pagination';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { FooterModule } from '../components/footer/footer.module';
import { SidebarModule } from '../components/sidebar/sidebar.module';
import { TitlebarModule } from '../components/titlebar/titlebar.module';
import { NoContentModule } from '../components/no-content/no-content.module';

@NgModule({
  bootstrap: [ AdjudicatorComponent ],
  declarations: [
    AdjudicatorComponent,
    LandingComponent,
    PenaltiesComponent,
    EntriesComponent,
    DisqualificationComponent,
    SwapEntriesComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    FooterModule,
    SidebarModule,
    TitlebarModule,
    NoContentModule,
    Ng2PaginationModule,
    NgbModule.forRoot(),
    TranslateModule.forRoot(),
    RouterModule.forRoot(
      adjudicatorRoutes, { useHash: false, preloadingStrategy: PreloadAllModules}
    )
  ],
  providers: [
    ENV_PROVIDERS,
    RacesService,
    RacesHttpService,
    EntriesService,
    EntriesHttpService,
    TimesService,
    TimesHttpService,
    ConfigService,
    PenaltiesHttpService,
    PenaltiesService,
    DisqualificationHttpService,
    DisqualificationService,
    AdjudicatorHttpDisqualificationsService,
    AdjudicatorDisqualificationService,
    AdjudicatorHttpPenaltiesService,
    AdjudicatorPenaltiesService,
    Title
  ]
})
export class AdjudicatorModule {
}
