import { Component, OnInit, OnDestroy } from '@angular/core';
import { Title } from '@angular/platform-browser';

import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';

import { TranslateService } from '@ngx-translate/core';

import { Idle, DEFAULT_INTERRUPTSOURCES } from '@ng-idle/core';
import { Keepalive } from '@ng-idle/keepalive';

import { AdjudicatorRacesService } from './races/races.service';
import { Race } from '../races/race.model';
import { ConverstationMessage } from '../messages/converstation-message.model';
import { Connected } from '../messages/connected.model';
import { Message } from '../websocket/message.model';
import { MessageType } from '../websocket/message-type.model';

import { AdjudicatorWebSocketService } from './websocket/websocket-service';

@Component({
  selector: 'adjudicator',
  styleUrls: [
    './adjudicator.component.css'
  ],
  templateUrl: './adjudicator.component.html'
})
export class AdjudicatorComponent implements OnInit, OnDestroy {
  public races: Observable<Race[]>;
  public message: ConverstationMessage;
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

    this.wsSubscription = this.ws.subscribeForMessages()
      .subscribe((message: ConverstationMessage) => {
      this.message = message;
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
    const message: Message = new Message();
    message.data = JSON.stringify(data);
    message.objectType = 'ConverstationMessage';
    message.type = MessageType.CHAT;
    this.ws.sendMessage(message);
  }
}
