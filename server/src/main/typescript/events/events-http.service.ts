import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Event, convertJsonToEvent } from './event.model';

@Injectable()
export class EventsHttpService {

  protected endpoint: string = '/rest/events';

  constructor(protected http: Http) {}

  public getEvents(): Observable<Event[]> {
    return this.http.get(this.endpoint)
      .map(convertJsonToEvents).share();
   }
}

function convertJsonToEvents(response: Response): Event[] {
    let jsonEvents: Event[] = response.json();
    let events: Event[] = new Array<Event>();
    jsonEvents.map((json: Event) => {
      events.push(convertJsonToEvent(json));
    });
    return events;
}
