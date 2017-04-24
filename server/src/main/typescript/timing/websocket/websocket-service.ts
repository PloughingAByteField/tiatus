import { Injectable } from '@angular/core';

import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subscription } from 'rxjs/Subscription';

import { Message, convertObjectToMessage } from '../../websocket/message.model';
import { ConverstationMessage } from '../../messages/converstation-message.model';
import { Connected } from '../../messages/connected.model';

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

    private messages: ConverstationMessage[];
    private subject: BehaviorSubject<ConverstationMessage[]>
        = new BehaviorSubject<ConverstationMessage[]>(this.messages);
    private connected: Connected[];

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

    public getMessages(): BehaviorSubject<ConverstationMessage[]> {
        return this.subject;
    }

    protected onMessage(data: string): void {
        console.log(data);
        let message: Message = convertObjectToMessage(JSON.parse(data));
        console.log(message);
        if (message.objectType === 'Penalty') {
          console.log('penalty');
        } else {
            super.onMessage(data);
        }
    }
}
