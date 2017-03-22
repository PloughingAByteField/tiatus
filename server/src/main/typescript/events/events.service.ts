import { Injectable, OnDestroy } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subscription } from 'rxjs/Subscription';

import { Event } from './event.model';
import { Race } from '../races/race.model';

import { EventsHttpService } from './events-http.service';

@Injectable()
export class EventsService implements OnDestroy {
    protected events: Event[] = new Array<Event>();
    protected subject: BehaviorSubject<Event[]>
        = new BehaviorSubject<Event[]>(this.events);

    private subscription: Subscription;

    constructor(protected service: EventsHttpService) {
        this.refresh();
    }

    public ngOnDestroy() {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
    }

    public getEvents(): BehaviorSubject<Event[]> {
        return this.subject;
    }

    public refresh(): void {
        this.subscription = this.service.getEvents().subscribe((events: Event[]) => {
            console.log(events);
            this.events = events;
            this.subject.next(this.events);
        });
    }
}
