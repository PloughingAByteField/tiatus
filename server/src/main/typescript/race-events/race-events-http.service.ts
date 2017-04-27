import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { RaceEvent, convertJsonToRaceEvent } from './race-event.model';
import { Race } from '../races/race.model';

@Injectable()
export class RaceEventsHttpService {

  protected endpoint: string = '/rest/events/assigned';

  constructor(protected http: Http) {}

  public getRaceEvents(): Observable<RaceEvent[]> {
    return this.http.get(this.endpoint)
      .map(convertJsonToRaceEvents).share();
  }
}

function convertJsonToRaceEvents(response: Response): RaceEvent[] {
    const jsonEvents: RaceEvent[] = response.json();
    const events: RaceEvent[] = new Array<RaceEvent>();
    jsonEvents.map((json: RaceEvent) => {
      events.push(convertJsonToRaceEvent(json));
    });
    return events;
}
