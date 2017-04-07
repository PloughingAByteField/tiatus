/*
 * Angular 2 decorators and services
 */
import { Component, ViewEncapsulation, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Title }     from '@angular/platform-browser';
import { RacesService } from '../races/races.service';
import { Observable } from 'rxjs/Observable';

import { Race } from '../races/race.model';
import { Position } from '../positions/position.model';

import { WebSocketService } from '../websocket/websocket-service';

import { TimingPositionService } from './times/timing-position.service';
/*
 * App Component
 * Top Level Component
 */
@Component({
  selector: 'timing',
  styleUrls: [
    './timing.component.css'
  ],
  templateUrl: './timing.component.html',
  providers: [ TimingPositionService ]
})
export class TimingComponent implements OnInit {
  public logo = '/assets/img/stopwatch.svg';
  public tiatusUrl = 'https://github.com/PloughingAByteField/tiatus';
  public param = { value: 'world' };
  public link = 'race';
  public races: Observable<Race[]>;
  public selectedPosition: Position;

  constructor(
    private translate: TranslateService,
    private titleService: Title,
    private racesService: RacesService,
    private timingPositionService: TimingPositionService,
    private ws: WebSocketService
  ) {
    translate.setDefaultLang('en');

    // the lang to use, if the lang isn't available, it will use the current loader to get them
    translate.use('en');
  }

  public ngOnInit() {
    this.timingPositionService.getPosition.subscribe((position: Position) => {
        if (position) {
          this.selectedPosition = position;
          this.races = this.racesService.getRaces();
        }
    });
  }

}
