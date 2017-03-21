import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';

import { UnassignedEventsHttpService } from
    '../../unassigned-events/unassigned-events-http.service';

@Injectable()
export class AdminUnassignedEventsHttpService extends UnassignedEventsHttpService {

    constructor(protected http: Http) {
        super(http);
    }

    // add remove update event and race event
}
