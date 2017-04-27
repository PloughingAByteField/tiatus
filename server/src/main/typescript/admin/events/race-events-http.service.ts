import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';

import { RaceEvent, convertJsonToRaceEvent } from '../../race-events/race-event.model';
import { RaceEventPojo } from './create-event/race-event-pojo.model';

import { RaceEventsHttpService } from '../../race-events/race-events-http.service';

@Injectable()
export class AdminRaceEventsHttpService extends RaceEventsHttpService {

    private headers = new Headers({ 'Content-Type': 'application/json' });

    constructor(protected http: Http) {
        super(http);
    }

    public createRaceEvent(pojo: RaceEventPojo): Promise<RaceEvent> {
        return this.http
            .post(this.endpoint,
            JSON.stringify(pojo), { headers: this.headers })
            .toPromise()
            .then((res: Response) => {
                const event: RaceEvent = convertJsonToRaceEvent(res.json());
                if (res.status === 201) {
                    const location: string = res.headers.get('location');
                    const locationParts = location.split('/');
                    const id: number = +locationParts[locationParts.length - 1];
                    event.id = id;
                }
                return event;
            })
            .catch((err) => Promise.reject(err));
    }

    public removeRaceEvent(raceEvent: RaceEvent): Promise<RaceEvent> {
        return this.http
            .delete(this.endpoint + '/' + raceEvent.id)
            .toPromise()
            .then(() => {
                return raceEvent;
            })
            .catch((err) => Promise.reject(err));
    }
}
