import { Injectable, OnDestroy } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Subscription } from 'rxjs';

import { RaceEvent } from './race-event.model';
import { Race } from '../races/race.model';

import { RaceEventsHttpService } from './race-events-http.service';

@Injectable()
export class RaceEventsService implements OnDestroy {
    protected events: RaceEvent[] = new Array<RaceEvent>();
    protected subject: BehaviorSubject<RaceEvent[]>
        = new BehaviorSubject<RaceEvent[]>(this.events);
    protected requested: boolean = false;
    protected subscription: Subscription;

    constructor(protected service: RaceEventsHttpService) {}

    public ngOnDestroy() {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
    }

    public getEvents(): BehaviorSubject<RaceEvent[]> {
        if (!this.requested) {
            this.requested = true;
            this.refresh();
        }
        return this.subject;
    }

    public getEventsForRace(race: Race): RaceEvent[] {
        if (race) {
            return this.events.filter((raceEvent: RaceEvent) => raceEvent.race === race.id);
        }
        return new Array<RaceEvent>();
    }

    public refresh(): void {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
        this.subscription = this.service.getRaceEvents().subscribe((events: RaceEvent[]) => {
            if (events != null) {
                this.events = events;
                this.subject.next(this.events);
            }
        });
    }
}
