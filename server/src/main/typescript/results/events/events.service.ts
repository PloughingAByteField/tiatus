import { Injectable } from '@angular/core';

import { EventsService } from '../../events/events.service';
import { ResultsHttpEventsService } from './events-http.service';

import { Event } from '../../events/event.model';

@Injectable()
export class ResultsEventsService extends EventsService {

    constructor(protected service: ResultsHttpEventsService) {
        super(service);
    }

    public refresh(): void {
        this.service.getEvents().subscribe((events: Event[]) => {
            if (events != null) {
                this.events = events;
                this.subject.next(this.events);
            }
        });
    }
}
