import { Injectable } from '@angular/core';

import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subscription } from 'rxjs/Subscription';

import { Message, convertObjectToMessage } from '../../websocket/message.model';
import { MessageType } from '../../websocket/message-type.model';
import { ConverstationMessage } from '../../messages/converstation-message.model';

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

    private messages: ConverstationMessage[];
    private subject: BehaviorSubject<ConverstationMessage[]>
        = new BehaviorSubject<ConverstationMessage[]>(this.messages);

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

    public getMessages(): BehaviorSubject<ConverstationMessage[]> {
        return this.subject;
    }

    protected onMessage(data: string): void {
        console.log(data);
        let message: Message = convertObjectToMessage(JSON.parse(data));
        console.log(message);
        if (message.type === MessageType.CONNECTED) {
            console.log('have connected');

        } else if (message.objectType === 'Penalty') {
            this.penaltiesService.processPenaltyMessage(message);

        } else if (message.objectType === 'Disqualification') {
            this.disqualificationService.processDisqualificationMessage(message);

        } else {
            super.onMessage(data);
        }
    }
}
