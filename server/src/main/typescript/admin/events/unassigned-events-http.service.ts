import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Event } from '../../events/event.model';

import { UnassignedEventsHttpService } from '../../unassigned-events/unassigned-events-http.service';

@Injectable()
export class AdminUnassignedEventsHttpService extends UnassignedEventsHttpService {

    constructor(protected http: HttpClient) {
        super(http);
    }

    public removeEvent(event: Event): Promise<Event> {
        return this.http
            .delete(this.endPoint + '/' + event.id)
            .toPromise()
            .then(() => {
                return event;
            })
            .catch((err) => Promise.reject(err));
    }
}
