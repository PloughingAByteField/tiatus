import { Injectable } from '@angular/core';

import { UnassignedEventsService } from '../../unassigned-events/unassigned-events.service';
import { AdminUnassignedEventsHttpService } from './unassigned-events-http.service';

import { Event } from '../../events/event.model';

@Injectable()
export class AdminUnassignedEventsService extends UnassignedEventsService {

    constructor(protected service: AdminUnassignedEventsHttpService) {
        super(service);
        this.refresh();
    }

    public addEvent(event: Event): void {
        this.events.push(event);
        this.subject.next(this.events);
    }

}
