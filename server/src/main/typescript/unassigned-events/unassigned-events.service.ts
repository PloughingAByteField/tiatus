import { Injectable, OnDestroy } from '@angular/core';

import { Observable } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { Subscription } from 'rxjs';

import { Event } from '../events/event.model';
import { Race } from '../races/race.model';
import { Data } from '../model/data.model';

import { UnassignedEventsHttpService } from './unassigned-events-http.service';

@Injectable()
export class UnassignedEventsService implements OnDestroy {
    protected events: Event[] = new Array<Event>();
    protected subject: BehaviorSubject<Event[]>
        = new BehaviorSubject<Event[]>(this.events);
    protected requested: boolean = false;

    private subscription: Subscription;

    constructor(protected service: UnassignedEventsHttpService) {}

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
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
        this.subscription = this.service.getUnassignedEvents().subscribe((data: Data) => {
            if (data.data !== undefined) {
                this.events = data.data;
                this.subject.next(this.events);
            }
        });
    }
}
