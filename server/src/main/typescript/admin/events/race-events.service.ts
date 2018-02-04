import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';

import { RaceEvent } from '../../race-events/race-event.model';
import { RaceEventPojo } from './create-event/race-event-pojo.model';

import { RaceEventsService } from '../../race-events/race-events.service';
import { AdminRaceEventsHttpService } from './race-events-http.service';
import { AdminEventsService } from './events.service';

@Injectable()
export class AdminRaceEventsService extends RaceEventsService {

    constructor(
        protected service: AdminRaceEventsHttpService,
        private eventsService: AdminEventsService) {
            super(service);
    }

    public createRaceEvent(pojo: RaceEventPojo): Observable<RaceEvent> {
        return this.service.createRaceEvent(pojo);
        // return new Promise((resolve) => this.service.createRaceEvent(pojo)
        //     .then((e: RaceEvent) => {
        //         pojo.event.id = e.event;
        //         this.eventsService.addEvent(pojo.event);
        //         this.events.push(e);
        //         this.subject.next(this.events);
        //         resolve(e);
        //     })
        // );
    }

    public removeRaceEvent(raceEvent: RaceEvent): void {
        this.service.removeRaceEvent(raceEvent).then((e: RaceEvent) => {
            const index = this.events.indexOf(raceEvent);
            this.events.splice(index, 1);
            this.subject.next(this.events);
        });
    }
}
