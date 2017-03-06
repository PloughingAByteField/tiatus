import { NgModule, ApplicationRef } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule, PreloadAllModules } from '@angular/router';
import { TranslateModule } from 'ng2-translate';

import { ENV_PROVIDERS } from './environment';

import { RacesService } from '../races/races.service';
import { RacesHttpService } from '../races/races-http.service';
import { ConfigService } from '../config/config.service';
import { ConfigHttpService } from '../config/config-http.service';

import { adminRoutes } from './admin.routes';
import { AdminComponent } from './admin.component';
import { LandingComponent } from './landing';
import { MenuComponent } from './menu';

import { ClubsComponent } from './clubs';
import { ConfigComponent } from './config';
import { DrawComponent } from './draw';
import { EntriesComponent } from './entries';
import { EventsComponent } from './events';
import { PositionsComponent } from './positions';
import { RacesComponent } from './races';
import { UsersComponent } from './users';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { FooterModule } from '../components/footer/footer.module';
import { SidebarModule } from '../components/sidebar/sidebar.module';
import { TitlebarModule } from '../components/titlebar/titlebar.module';
import { NoContentModule } from '../components/no-content/no-content.module';

@NgModule({
  bootstrap: [ AdminComponent ],
  declarations: [
    AdminComponent,
    LandingComponent,
    MenuComponent,
    ClubsComponent,
    ConfigComponent,
    DrawComponent,
    EntriesComponent,
    EventsComponent,
    PositionsComponent,
    RacesComponent,
    UsersComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    FooterModule,
    SidebarModule,
    TitlebarModule,
    NoContentModule,
    NgbModule.forRoot(),
    TranslateModule.forRoot(),
    RouterModule.forRoot(
      adminRoutes, { useHash: false, preloadingStrategy: PreloadAllModules}
    )
  ],
  providers: [
    ENV_PROVIDERS,
    RacesService,
    RacesHttpService,
    ConfigService,
    ConfigHttpService,
    Title
  ]
})
export class AdminModule {
}
