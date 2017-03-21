import { Injectable } from '@angular/core';

import { Event } from '../../events/event.model';
import { EventsService } from '../../events/events.service';
import { AdminEventsHttpService } from './events-http.service';

@Injectable()
export class AdminEventsService extends EventsService {

    constructor(protected service: AdminEventsHttpService) {
        super(service);
    }

    public addEvent(event: Event): void {
        this.events.push(event);
    }

    public createEvent(event: Event): Promise<Event> {
        return new Promise((resolve) => this.service.createEvent(event).then((e: Event) => {
            this.events.push(e);
            this.subject.next(this.events);
            resolve(e);
        }));
    }
}
