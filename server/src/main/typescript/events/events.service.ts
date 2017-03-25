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
    protected requested: boolean = false;

    private subscription: Subscription;

    constructor(protected service: EventsHttpService) {}

    public ngOnDestroy() {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
    }

    public getEvents(): BehaviorSubject<Event[]> {
        if (!this.requested) {
            this.requested = true;
            this.refresh();
        }
        return this.subject;
    }

    public refresh(): void {
        this.subscription = this.service.getEvents().subscribe((events: Event[]) => {
            this.events = events;
            this.subject.next(this.events);
        });
    }
}
