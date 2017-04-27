import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Event, convertObjectToEvent } from './event.model';

@Injectable()
export class EventsHttpService {

  protected endpoint: string = '/rest/events';

  constructor(protected http: Http) {}

  public getEvents(): Observable<Event[]> {
    return this.http.get(this.endpoint)
      .map(convertResponseToEvents).share();
   }
}

export function convertResponseToEvents(response: Response): Event[] {
    const jsonEvents: Event[] = response.json();
    const events: Event[] = new Array<Event>();
    jsonEvents.map((event: Event) => {
      events.push(convertObjectToEvent(event));
    });
    return events;
}
