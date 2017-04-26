import { Component, OnInit, OnDestroy } from '@angular/core';
import { Title }     from '@angular/platform-browser';

import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';

import { TranslateService } from '@ngx-translate/core';

import { Idle, DEFAULT_INTERRUPTSOURCES } from '@ng-idle/core';
import { Keepalive } from '@ng-idle/keepalive';

import { Race } from '../races/race.model';
import { Position } from '../positions/position.model';
import { RacesService } from '../races/races.service';

import { ConverstationMessage } from '../messages/converstation-message.model';
import { Connected } from '../messages/connected.model';
import { Message } from '../websocket/message.model';
import { MessageType } from '../websocket/message-type.model';
import { TimingWebSocketService } from './websocket/websocket-service';

import { TimingPositionService } from './times/timing-position.service';

@Component({
  selector: 'timing',
  styleUrls: [
    './timing.component.css'
  ],
  templateUrl: './timing.component.html',
  providers: [ TimingPositionService ]
})
export class TimingComponent implements OnInit, OnDestroy {
  public link = 'race';
  public races: Observable<Race[]>;
  public selectedPosition: Position;
  public message: ConverstationMessage;
  public connected: Connected[];

  private connectedSubscription: Subscription;
  private wsSubscription: Subscription;

  constructor(
    private translate: TranslateService,
    private titleService: Title,
    private idle: Idle,
    private keepalive: Keepalive,
    private racesService: RacesService,
    private timingPositionService: TimingPositionService,
    private ws: TimingWebSocketService
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
          // send selected position to server
          let connected: Message = new Message();
          connected.data = JSON.stringify(position);
          connected.objectType = 'Position';
          connected.type = MessageType.CONNECTED;
          this.ws.sendMessage(connected);
        }
    });

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
    let message: Message = new Message();
    message.data = JSON.stringify(data);
    message.objectType = 'ConverstationMessage';
    message.type = MessageType.CHAT;
    this.ws.sendMessage(message);
  }
}
