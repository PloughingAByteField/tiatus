import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';

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

    public createRaceEvent(raceEvent: RaceEventPojo): Promise<RaceEventPojo> {
        return new Promise((resolve) => this.service.createRaceEvent(raceEvent).then((raceEventPojo: RaceEventPojo) => {
            resolve(raceEventPojo);
        }));
    }

    public removeRaceEvent(raceEvent: RaceEvent): void {
        this.service.removeRaceEvent(raceEvent).then((e: RaceEvent) => {
            const index = this.events.indexOf(raceEvent);
            this.events.splice(index, 1);
            this.subject.next(this.events);
        });
    }
}
