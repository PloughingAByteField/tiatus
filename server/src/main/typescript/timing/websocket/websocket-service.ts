import { Injectable } from '@angular/core';

import { BehaviorSubject } from 'rxjs';
import { Subscription } from 'rxjs';

import { Message, convertObjectToMessage } from '../../websocket/message.model';
import { MessageType } from '../../websocket/message-type.model';
import { ConverstationMessage, convertObjectToConverstationMessage } from '../../messages/converstation-message.model';
import { Connected, convertObjectToConnected } from '../../messages/connected.model';

import { WebSocketService } from '../../websocket/websocket-service';
import { WebSocketWSService } from '../../websocket/websocket-ws-service';
import { RacesService } from '../../races/races.service';
import { PenaltiesService } from '../../penalties/penalties.service';
import { DisqualificationService } from '../../disqualification/disqualification.service';

import { ClubsService } from '../../clubs/clubs.service';
import { EntriesService } from '../../entries/entries.service';
import { EventsService } from '../../events/events.service';
import { PositionsService } from '../../positions/positions.service';

@Injectable()
export class TimingWebSocketService extends WebSocketService {

    private message: ConverstationMessage = null;
    private subject: BehaviorSubject<ConverstationMessage>
        = new BehaviorSubject<ConverstationMessage>(this.message);
    private connected: Connected[] = new Array<Connected>();
    private connectedSubject: BehaviorSubject<Connected[]>
        = new BehaviorSubject<Connected[]>(this.connected);

    constructor(
        protected clubsService: ClubsService,
        protected entriesService: EntriesService,
        protected eventsService: EventsService,
        protected positionsService: PositionsService,
        protected racesService: RacesService,
        protected penaltiesService: PenaltiesService,
        protected disqualificationService: DisqualificationService,
        protected ws: WebSocketWSService) {
            super(clubsService, entriesService, eventsService, positionsService, racesService, ws);
    }

    public subscribeForMessages(): BehaviorSubject<ConverstationMessage> {
        return this.subject;
    }

    public getConnectedPositions(): BehaviorSubject<Connected[]> {
        return this.connectedSubject;
    }

    protected onMessage(data: string): void {
        const message: Message = convertObjectToMessage(JSON.parse(data));
        if (message.type === MessageType.CONNECTED) {
            const connected: Connected = convertObjectToConnected(JSON.parse(message.data));
            let update: boolean = false;
            for (const currentlyConnected of this.connected) {
                if (currentlyConnected.userName === connected.userName
                && currentlyConnected.role === connected.role) {
                    update = true;
                    currentlyConnected.position = connected.position;
                }
            }
            if (!update) {
                this.connected.push(connected);
            }
            this.connectedSubject.next(this.connected);

        } else if (message.type === MessageType.DISCONNECTED) {
            const disconnected: Connected = convertObjectToConnected(JSON.parse(message.data));
            let toRemove: Connected;
            for (const currentlyConnected of this.connected) {
                if (currentlyConnected.userName === disconnected.userName
                && currentlyConnected.role === disconnected.role
                && currentlyConnected.position === disconnected.position) {
                    toRemove = disconnected;
                    break;
                }
            }
            if (toRemove) {
                const index: number = this.connected.indexOf(toRemove);
                this.connected.splice(index, 1);
                this.connectedSubject.next(this.connected);
            }

        } else if (message.type === MessageType.CHAT) {
            this.message = convertObjectToConverstationMessage(message.data);
            this.subject.next(this.message);

        } else {
            super.onMessage(data);
        }
    }
}
