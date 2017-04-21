/*
 * Angular 2 decorators and services
 */
import { Component, ViewEncapsulation, OnInit } from '@angular/core';
import { Title }     from '@angular/platform-browser';

import { Observable } from 'rxjs/Observable';

import { TranslateService } from '@ngx-translate/core';

import { Idle, DEFAULT_INTERRUPTSOURCES } from '@ng-idle/core';
import { Keepalive } from '@ng-idle/keepalive';

import { RacesService } from '../races/races.service';
import { Race } from '../races/race.model';

import { WebSocketService } from '../websocket/websocket-service';

/*
 * App Component
 * Top Level Component
 */
@Component({
  selector: 'adjudicator',
  styleUrls: [
    './adjudicator.component.css'
  ],
  templateUrl: './adjudicator.component.html'
})
export class AdjudicatorComponent implements OnInit {
  public races: Observable<Race[]>;

  constructor(
    private translate: TranslateService,
    private idle: Idle,
    private keepalive: Keepalive,
    private titleService: Title,
    private racesService: RacesService,
    private ws: WebSocketService
  ) {}

  public ngOnInit() {
    this.translate.use('en');

    this.idle.setIdle(600);
    this.idle.setTimeout(300);
    this.idle.setInterrupts(DEFAULT_INTERRUPTSOURCES);
    this.keepalive.interval(360);
    this.keepalive.request('/rest/keepalive');
    this.idle.watch();
    this.idle.onTimeout.subscribe(() => {
      console.log('Timed out');
      window.location.href = '/rest/logout';
    });

    this.races = this.racesService.getRaces();
  }
}
