import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';

import { RaceEvent, convertJsonToRaceEvent } from '../../race-events/race-event.model';
import { RaceEventPojo } from './create-event/race-event-pojo.model';

import { RaceEventsHttpService } from '../../race-events/race-events-http.service';

@Injectable()
export class AdminRaceEventsHttpService extends RaceEventsHttpService {

    private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    constructor(protected http: HttpClient) {
        super(http);
    }

    public createRaceEvent(pojo: RaceEventPojo): Observable<RaceEvent> {
        return this.http
            .post<RaceEvent>(this.endPoint,
            JSON.stringify(pojo), { headers: this.headers });
            // .toPromise()
            // .then((res: Response) => {
            //     const event: RaceEvent = convertJsonToRaceEvent(res);
            //     if (res.status === 201) {
            //         const location: string = res.headers.get('location');
            //         const locationParts = location.split('/');
            //         const id: number = +locationParts[locationParts.length - 1];
            //         event.id = id;
            //     }
            //     return event;
            // })
            // .catch((err) => Promise.reject(err));
    }

    public removeRaceEvent(raceEvent: RaceEvent): Promise<RaceEvent> {
        return this.http
            .delete(this.endPoint + '/' + raceEvent.id)
            .toPromise()
            .then(() => {
                return raceEvent;
            })
            .catch((err) => Promise.reject(err));
    }
}
