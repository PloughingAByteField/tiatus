import { Component, ViewEncapsulation, OnInit } from '@angular/core';
import { Title }     from '@angular/platform-browser';

import { Observable } from 'rxjs/Observable';

import { TranslateService } from '@ngx-translate/core';

import { Idle, DEFAULT_INTERRUPTSOURCES } from '@ng-idle/core';
import { Keepalive } from '@ng-idle/keepalive';

import { Race } from '../races/race.model';
import { Position } from '../positions/position.model';
import { RacesService } from '../races/races.service';
import { WebSocketService } from '../websocket/websocket-service';

import { TimingPositionService } from './times/timing-position.service';

@Component({
  selector: 'timing',
  styleUrls: [
    './timing.component.css'
  ],
  templateUrl: './timing.component.html',
  providers: [ TimingPositionService ]
})
export class TimingComponent implements OnInit {
  public link = 'race';
  public races: Observable<Race[]>;
  public selectedPosition: Position;

  constructor(
    private translate: TranslateService,
    private titleService: Title,
    private idle: Idle,
    private keepalive: Keepalive,
    private racesService: RacesService,
    private timingPositionService: TimingPositionService,
    private ws: WebSocketService
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

    this.timingPositionService.getPosition.subscribe((position: Position) => {
        if (position) {
          this.selectedPosition = position;
          this.races = this.racesService.getRaces();
        }
    });
  }

}
