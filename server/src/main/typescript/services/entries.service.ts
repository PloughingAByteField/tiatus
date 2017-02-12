import { EventEmitter, Injectable } from '@angular/core';
import { Http, URLSearchParams, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Entry } from '../models/entry.model';
import { Race } from '../models/race.model';

@Injectable()
export class EntriesService {
  public searchEvent: EventEmitter<any> = new EventEmitter();

  constructor(private http: Http) {}

  public getEntries(): Observable<Entry[]> {
    return this.http.get('/rest/entries')
      .map((response) => response.json()).share();
  }

  public getEntriesForRace(race: Race): Observable<Entry[]> {
  return this.http.get('/rest/entries/race/' + race.id)
    .map((response) => response.json()).share();
  }

}
