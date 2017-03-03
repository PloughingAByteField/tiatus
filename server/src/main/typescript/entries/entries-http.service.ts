import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Entry, convertJsonToEntry } from './entry.model';
import { Race } from '../models/race.model';

@Injectable()
export class EntriesHttpService {
  constructor(private http: Http) {}

  public getEntries(): Observable<Entry[]> {
    return this.http.get('/rest/entries')
      .map(convertJsonToEntries).share();
  }

  public getEntriesForRace(race: Race): Observable<Entry[]> {
  return this.http.get('/rest/entries/race/' + race.id)
    .map(convertJsonToEntries).share();
  }
 }

function convertJsonToEntries(response: Response): Entry[] {
    let jsonEntries: Entry[] = response.json();
    let entries: Entry[] = new Array<Entry>();
    jsonEntries.map((json: Entry) => {
      entries.push(convertJsonToEntry(json));
    });
    return entries;
}
