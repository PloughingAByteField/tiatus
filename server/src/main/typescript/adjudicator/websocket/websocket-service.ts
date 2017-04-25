import { Injectable } from '@angular/core';

import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subscription } from 'rxjs/Subscription';

import { Message, convertObjectToMessage } from '../../websocket/message.model';
import { MessageType } from '../../websocket/message-type.model';
import { ConverstationMessage } from '../../messages/converstation-message.model';
import { Connected, convertObjectToConnected } from '../../messages/connected.model';

import { WebSocketService } from '../../websocket/websocket-service';
import { WebSocketWSService } from '../../websocket/websocket-ws-service';
import { AdjudicatorRacesService } from '../races/races.service';
import { AdjudicatorPenaltiesService } from '../penalties/penalties.service';
import { AdjudicatorDisqualificationService } from '../disqualification/disqualification.service';

import { ClubsService } from '../../clubs/clubs.service';
import { EntriesService } from '../../entries/entries.service';
import { EventsService } from '../../events/events.service';
import { PositionsService } from '../../positions/positions.service';

@Injectable()
export class AdjudicatorWebSocketService extends WebSocketService {

    private message: ConverstationMessage;
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
        protected racesService: AdjudicatorRacesService,
        protected penaltiesService: AdjudicatorPenaltiesService,
        protected disqualificationService: AdjudicatorDisqualificationService,
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
        console.log(data);
        let message: Message = convertObjectToMessage(JSON.parse(data));
        console.log(message);
        if (message.type === MessageType.CONNECTED) {
            console.log('have connected');
            console.log(JSON.parse(message.data));
            let connected: Connected = convertObjectToConnected(JSON.parse(message.data));
            console.log(connected);
            let update: boolean = false;
            for (let currentlyConnected of this.connected) {
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
            console.log('have disconnected');
            let disconnected: Connected = convertObjectToConnected(JSON.parse(message.data));
            console.log(disconnected);
            let toRemove: Connected;
            for (let currentlyConnected of this.connected) {
                if (currentlyConnected.userName === disconnected.userName
                && currentlyConnected.role === disconnected.role
                && currentlyConnected.position === disconnected.position) {
                    toRemove = currentlyConnected;
                    break;
                }
            }
            if (toRemove) {
                let index: number = this.connected.indexOf(toRemove);
                console.log(index);
                this.connected.splice(index, 1);
                console.log(this.connected);
                this.connectedSubject.next(this.connected);
            }

        } else if (message.objectType === 'Penalty') {
            this.penaltiesService.processPenaltyMessage(message);

        } else if (message.objectType === 'Disqualification') {
            this.disqualificationService.processDisqualificationMessage(message);

        } else {
            super.onMessage(data);
        }
    }
}
