import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Entry, convertObjectToEntry } from './entry.model';
import { Race } from '../races/race.model';

@Injectable()
export class EntriesHttpService {
  constructor(protected http: Http) {}

  public getEntries(): Observable<Entry[]> {
    return this.http.get('/rest/entries')
      .map(convertResponseToEntries).share();
  }

  public getEntriesForRace(race: Race): Observable<Entry[]> {
    return this.http.get('/rest/entries/race/' + race.id)
      .map(convertResponseToEntries).share();
  }
 }

function convertResponseToEntries(response: Response): Entry[] {
    let jsonEntries: Entry[] = response.json();
    let entries: Entry[] = new Array<Entry>();
    jsonEntries.map((entry: Entry) => {
      entries.push(convertObjectToEntry(entry));
    });
    return entries;
}
