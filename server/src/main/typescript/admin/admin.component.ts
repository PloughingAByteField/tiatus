import { Component, OnInit } from '@angular/core';

import { TranslateService } from '@ngx-translate/core';

import { Idle, DEFAULT_INTERRUPTSOURCES } from '@ng-idle/core';
import { Keepalive } from '@ng-idle/keepalive';

import { AdminWebSocketService } from './websocket/websocket-service';

@Component({
  selector: 'admin',
  styleUrls: [
    './admin.component.css'
  ],
  templateUrl: './admin.component.html'
})
export class AdminComponent implements OnInit {

  constructor(
    private translate: TranslateService,
    private idle: Idle,
    private keepalive: Keepalive,
    private ws: AdminWebSocketService
  ) {}

  public ngOnInit() {
    this.idle.setIdle(600);
    this.idle.setTimeout(300);
    this.idle.setInterrupts(DEFAULT_INTERRUPTSOURCES);
    this.keepalive.interval(360);
    this.keepalive.request('/rest/keepalive');
    this.idle.watch();
    this.idle.onTimeout.subscribe(() => {
      window.location.href = '/rest/logout';
    });

    this.translate.setDefaultLang('en');
    this.translate.use('en');
  }
}
