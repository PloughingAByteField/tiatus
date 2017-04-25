import { Component, ViewEncapsulation, OnInit } from '@angular/core';
import { Title }     from '@angular/platform-browser';

import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';

import { TranslateService } from '@ngx-translate/core';

import { Idle, DEFAULT_INTERRUPTSOURCES } from '@ng-idle/core';
import { Keepalive } from '@ng-idle/keepalive';

import { AdjudicatorRacesService } from './races/races.service';
import { Race } from '../races/race.model';
import { ConverstationMessage } from '../messages/converstation-message.model';
import { Connected } from '../messages/connected.model';

import { AdjudicatorWebSocketService } from './websocket/websocket-service';

@Component({
  selector: 'adjudicator',
  styleUrls: [
    './adjudicator.component.css'
  ],
  templateUrl: './adjudicator.component.html'
})
export class AdjudicatorComponent implements OnInit {
  public races: Observable<Race[]>;
  public messages: ConverstationMessage[];
  public connected: Connected[];

  private connectedSubscription: Subscription;
  private wsSubscription: Subscription;

  constructor(
    private translate: TranslateService,
    private idle: Idle,
    private keepalive: Keepalive,
    private titleService: Title,
    private racesService: AdjudicatorRacesService,
    private ws: AdjudicatorWebSocketService
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

    this.wsSubscription = this.ws.getMessages().subscribe((messages: ConverstationMessage[]) => {
      this.messages = messages;
    });
    this.connectedSubscription = this.ws.getConnectedPositions()
      .subscribe((connected: Connected[]) => {
      this.connected = connected;
    });
  }

  public ngOnDestroy() {
    this.connectedSubscription.unsubscribe();
    this.wsSubscription.unsubscribe();
  }

  public onNewMessage(data: ConverstationMessage): void {
    console.log('got new message');
    console.log(data);
  }
}
