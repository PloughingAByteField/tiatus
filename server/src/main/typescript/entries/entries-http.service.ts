import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';

import { Race } from '../races/race.model';
import { Entry } from './entry.model';

@Injectable()
export class EntriesHttpService {

  constructor(protected http: HttpClient) { }

  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
  
  public getEntries(): Observable<Entry[]> {
    return this.http.get<Entry[]>('/rest/entries', this.httpOptions);
  }

  public getEntriesForRace(race: Race): Observable<Entry[]> {
    return this.http.get<Entry[]>('/rest/entries/race/' + race.id, this.httpOptions);
  }
 }
