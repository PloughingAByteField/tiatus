import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';

import { Event, convertObjectToEvent } from '../../events/event.model';

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
                const newEvent: Event = convertObjectToEvent(res.json());
                if (res.status === 201) {
                    const location: string = res.headers.get('location');
                    const locationParts = location.split('/');
                    const id: number = +locationParts[locationParts.length - 1];
                    newEvent.id = id;
                }
                return newEvent;
            })
            .catch((err) => Promise.reject(err));
    }

    public updateEvent(event: Event): Promise<Event> {
        return this.http
            .put(this.endpoint + '/' + event.id,
            JSON.stringify(event), { headers: this.headers })
            .toPromise()
            .then((res: Response) => {
                const updatedEvent: Event = convertObjectToEvent(res.json());
                return updatedEvent;
            })
            .catch((err) => Promise.reject(err));
    }
}
