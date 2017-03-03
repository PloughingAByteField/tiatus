import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { Event } from './event.model';

import { EventsHttpService } from './events-http.service';

@Injectable()
export class EventsService {
    protected events: Event[] = new Array<Event>();
    protected subject: BehaviorSubject<Event[]>
        = new BehaviorSubject<Event[]>(this.events);

    constructor(protected service: EventsHttpService) {
        this.refresh();
    }

    public getEvents(): BehaviorSubject<Event[]> {
        return this.subject;
    }

    public refresh(): void {
        this.service.getEvents().subscribe((events: Event[]) => {
            this.events = events;
            this.subject.next(this.events);
        });
    }
}
