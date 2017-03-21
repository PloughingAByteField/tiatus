import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';

import { RaceEventsHttpService } from '../../race-events/race-events-http.service';

@Injectable()
export class AdminRaceEventsHttpService extends RaceEventsHttpService {

    constructor(protected http: Http) {
        super(http);
    }

    // add remove update event and race event
}
