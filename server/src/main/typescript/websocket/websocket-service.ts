import { Injectable, OnInit } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subscription } from 'rxjs/Subscription';

import { Message, convertObjectToMessage } from './message.model';

import { ClubsService } from '../clubs/clubs.service';
import { EntriesService } from '../entries/entries.service';
import { EventsService } from '../events/events.service';
import { PositionsService } from '../positions/positions.service';
import { RacesService } from '../races/races.service';
import { WebSocketWSService } from './websocket-ws-service';

@Injectable()
export class WebSocketService {

    constructor(
        protected clubsService: ClubsService,
        protected entriesService: EntriesService,
        protected eventsService: EventsService,
        protected positionsService: PositionsService,
        protected racesService: RacesService,
        protected ws: WebSocketWSService) {
        let url: string = window.location.host;
        let secure: boolean = false;
        let protocol: string = window.location.protocol;
        if (protocol === 'https:') {
            secure = true;
        }
        this.subscribeToUrl(url, secure);
    }

    public subscribeToUrl(url: string, secure: boolean): void {
        let wsUrl: string;
        if (secure) {
            wsUrl = 'wss://' + url + '/ws';
        } else {
            wsUrl = 'ws://' + url + '/ws';
        }
        console.log(wsUrl);
        this.ws.createWebSocket(wsUrl).subscribe(
            (data: string) => this.onMessage(data),
            (err) => console.log(err),
            () => console.log('websocket is closed'));
    }

    public sendMessage(message: any): void {
        if (this.ws) {
            this.ws.sendMessage(message);
        }
    }

    protected onMessage(data: string): void {
        let message: Message = convertObjectToMessage(JSON.parse(data));
        if (message.objectType === 'Race') {
            this.racesService.processMessage(message);
        } else if (message.objectType === 'Position') {
            this.positionsService.processMessage(message);
        } else if (message.objectType === 'Club') {
            this.clubsService.processMessage(message);
        } else if (message.objectType === 'Entry') {
            this.entriesService.processMessage(message);
        } else if (message.objectType === 'Event') {
            this.eventsService.processMessage(message);
        }
    }
}
