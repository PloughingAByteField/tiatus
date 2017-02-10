import { EventEmitter, Injectable } from '@angular/core';
import { Http, URLSearchParams, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { PositionTime } from '../models/postion-time.model';
import { Race } from '../models/race.model';
import { Position } from '../models/position.model';

@Injectable()
export class TimesService {
  public searchEvent: EventEmitter<any> = new EventEmitter();

  constructor(private http: Http) {}

  public getTimesForPositionInRace(position: Position, race: Race): Observable<PositionTime[]> {
    if (position && race) {
      return this.http.get('/rest/time/position/' + position.id + '/race/' + race.id)
        .map((response) => response.json());
    }
  }

}
