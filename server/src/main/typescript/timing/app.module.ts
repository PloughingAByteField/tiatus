import { NgModule, ApplicationRef } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule, PreloadAllModules } from '@angular/router';
import { TranslateModule } from 'ng2-translate';
import { RacesService } from '../services/races.service';
import { EntriesService } from '../services/entries.service';

import { ENV_PROVIDERS } from './environment';
import { ROUTES } from './app.routes';
import { AppComponent } from './app.component';
import { LandingComponent } from './landing';
import { NoContentComponent } from './no-content';
import { TimingComponent } from './time_entry';

import { FooterComponent } from '../components/footer/footer.component';
import { TitlebarComponent } from '../components/titlebar/titlebar.component';
import { SidebarComponent } from '../components/sidebar/sidebar.component';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { Ng2SmartTableModule } from 'ng2-smart-table';

@NgModule({
  bootstrap: [ AppComponent ],
  declarations: [
    AppComponent,
    LandingComponent,
    NoContentComponent,
    FooterComponent,
    TitlebarComponent,
    SidebarComponent,
    TimingComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    Ng2SmartTableModule,
    NgbModule.forRoot(),
    TranslateModule.forRoot(),
    RouterModule.forRoot(ROUTES, { useHash: false, preloadingStrategy: PreloadAllModules })
  ],
  providers: [
    ENV_PROVIDERS,
    RacesService,
    EntriesService,
    Title
  ]
})
export class AppModule {
}

