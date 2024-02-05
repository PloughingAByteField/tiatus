import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { PositionTime } from './postion-time.model';
import { Race } from '../races/race.model';

@Injectable()
export class TimesHttpService {

  protected timeEndPoint: string = '/rest/time/race/';

  protected httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(protected http: HttpClient) {}

  public getTimesForRace(race: Race): Observable<PositionTime[]> {
    if (race) {
      const url: string = this.timeEndPoint + race.id;
      return this.http.get<PositionTime[]>(url, this.httpOptions);
    }
  }
}
