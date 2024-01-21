import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

import { CachedHttpService } from '../http/cached-http.service';
import { Data } from '../model/data.model';
import { Race } from '../races/race.model';

@Injectable()
export class EntriesHttpService extends CachedHttpService {
  constructor(protected http: HttpClient) {
    super(http);
  }

  public getEntries(): Observable<Data> {
    return this.getData('/rest/entries');
  }

  public getEntriesForRace(race: Race): Observable<Data> {
    return this.getData('/rest/entries/race/' + race.id);
  }
 }
