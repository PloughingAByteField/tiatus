import { Injectable } from '@angular/core';

import { WebSocketService } from '../../websocket/websocket-service';
import { WebSocketWSService } from '../../websocket/websocket-ws-service';
import { AdminRacesService } from '../races/races.service';

@Injectable()
export class AdminWebSocketService extends WebSocketService {

    constructor(
        protected racesService: AdminRacesService,
        protected ws: WebSocketWSService) {
        super(racesService, ws);
    }

}
