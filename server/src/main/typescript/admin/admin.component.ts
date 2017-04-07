/*
 * Angular 2 decorators and services
 */
import { Component, ViewEncapsulation } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { AdminWebSocketService } from './websocket/websocket-service';

/*
 * App Component
 * Top Level Component
 */
@Component({
  selector: 'admin',
  styleUrls: [
    './admin.component.css'
  ],
  templateUrl: './admin.component.html'
})
export class AdminComponent {
  public logo = '/assets/img/stopwatch.svg';
  public tiatusUrl = 'https://github.com/PloughingAByteField/tiatus';
  public param = {value: 'world'};

  constructor(
    private translate: TranslateService,
    private ws: AdminWebSocketService
  ) {
    translate.setDefaultLang('en');

    // the lang to use, if the lang isn't available, it will use the current loader to get them
    translate.use('en');
  }

}
