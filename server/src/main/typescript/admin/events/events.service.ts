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

    public updateEvent(event: Event): Promise<Event> {
        return new Promise((resolve) => this.service.updateEvent(event).then((e: Event) => {
            let eventBeingUpdated = this.getEventForId(e.id);
            if (eventBeingUpdated !== null) {
                eventBeingUpdated.name = e.name;
                eventBeingUpdated.positions = e.positions;
            }
            this.subject.next(this.events);
            resolve(e);
        }));
    }

    public getEventForId(eventId: number): Event {
        for (let event of this.events) {
            if (event.id === eventId) {
                return event;
            }
        }
        return null;
    }
}
