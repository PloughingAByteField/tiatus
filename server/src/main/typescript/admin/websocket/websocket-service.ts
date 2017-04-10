import { Injectable } from '@angular/core';

import { Message, convertObjectToMessage } from '../../websocket/message.model';

import { WebSocketService } from '../../websocket/websocket-service';
import { WebSocketWSService } from '../../websocket/websocket-ws-service';
import { AdminRacesService } from '../races/races.service';
import { AdminEntriesService } from '../entries/entries.service';
import { AdminClubsService } from '../clubs/clubs.service';
import { AdminEventsService } from '../events/events.service';
import { AdminUsersService } from '../users/users.service';
import { AdminPositionsService } from '../positions/positions.service';
import { RacePositionsService } from '../race-positions/race-positions.service';
import { RacePositionTemplatesService } from
    '../race-position-templates/race-position-templates.service';

@Injectable()
export class AdminWebSocketService extends WebSocketService {

    constructor(
        protected clubsService: AdminClubsService,
        protected entriesService: AdminEntriesService,
        protected eventsService: AdminEventsService,
        protected racesService: AdminRacesService,
        protected positionsService: AdminPositionsService,
        protected usersService: AdminUsersService,
        protected racePositionsService: RacePositionsService,
        protected racePositionTemplatesService: RacePositionTemplatesService,
        protected ws: WebSocketWSService) {
            super(clubsService, entriesService, eventsService, positionsService, racesService, ws);
    }

    protected onMessage(data: string): void {
        console.log(data);
        let message: Message = convertObjectToMessage(JSON.parse(data));
        console.log(message);
        if (message.objectType === 'User') {
            this.usersService.processUserMessage(message);

        } else if (message.objectType === 'RacePositionTemplate') {
            this.racePositionsService.processTemplateMessage(message);

        } else if (message.objectType === 'RacePositionTemplateEntry') {
            this.racePositionTemplatesService.processTemplateMessage(message);

        } else {
            super.onMessage(data);
        }
    }
}
