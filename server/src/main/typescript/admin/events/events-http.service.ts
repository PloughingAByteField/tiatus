import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';

import { Event, convertJsonToEvent } from '../../events/event.model';

import { EventsHttpService } from '../../events/events-http.service';

@Injectable()
export class AdminEventsHttpService extends EventsHttpService {

    private headers = new Headers({ 'Content-Type': 'application/json' });

    constructor(protected http: Http) {
        super(http);
    }

    public createEvent(event: Event): Promise<Event> {
        return this.http
            .post(this.endpoint,
            JSON.stringify(event), { headers: this.headers })
            .toPromise()
            .then((res: Response) => {
                let newEvent: Event = convertJsonToEvent(res.json());
                if (res.status === 201) {
                    let location: string = res.headers.get('location');
                    let locationParts = location.split('/');
                    let id: number = +locationParts[locationParts.length - 1];
                    newEvent.id = id;
                }
                return newEvent;
            })
            .catch((err) => Promise.reject(err));
    }
}
