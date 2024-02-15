import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';

import { Event } from '../../events/event.model';

import { EventsHttpService } from '../../events/events-http.service';

@Injectable()
export class AdminEventsHttpService extends EventsHttpService {

    private httpHeader = {
        observe: 'response' as const,
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    constructor(protected http: HttpClient) {
        super(http);
    }

    public createEvent(event: Event): Promise<Event> {
        console.log("Creating event " + event.name);
        return this.http
            .post(this.endPoint,
                event,
                this.httpHeader)
            .toPromise()
            .then((res: HttpResponse<Event>) => {
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

    public updateEvent(event: Event): Promise<Event> {
        return this.http
            .put(this.endPoint + '/' + event.id,
                event, 
                this.httpHeader)
            .toPromise()
            .then((res: HttpResponse<Event>) => {
                return event;
            })
            .catch((err) => Promise.reject(err));
    }
}
