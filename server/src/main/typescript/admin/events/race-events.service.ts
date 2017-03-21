import { Injectable } from '@angular/core';

import { RaceEventsService } from '../../race-events/race-events.service';
import { AdminRaceEventsHttpService } from './race-events-http.service';

@Injectable()
export class AdminRaceEventsService extends RaceEventsService {

    constructor(protected service: AdminRaceEventsHttpService) {
        super(service);
        this.refresh();
    }

}
