import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';

import { EventsHttpService } from '../../events/events-http.service';

@Injectable()
export class AdminEventsHttpService extends EventsHttpService {

    constructor(protected http: Http) {
        super(http);
    }

    // add remove update event and race event
}
