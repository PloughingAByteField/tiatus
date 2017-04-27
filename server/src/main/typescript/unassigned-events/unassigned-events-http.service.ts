import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Event, convertObjectToEvent } from '../events/event.model';

@Injectable()
export class UnassignedEventsHttpService {

  protected endpoint: string = '/rest/events/unassigned';

  constructor(protected http: Http) {}

  public getUnassignedEvents(): Observable<Event[]> {
    return this.http.get(this.endpoint)
      .map(convertJsonToUnassignedEvents).share();
   }
}

function convertJsonToUnassignedEvents(response: Response): Event[] {
    const jsonEvents: Event[] = response.json();
    const events: Event[] = new Array<Event>();
    jsonEvents.map((json: Event) => {
      events.push(convertObjectToEvent(json));
    });
    return events;
}
