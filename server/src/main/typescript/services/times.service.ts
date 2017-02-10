import { EventEmitter, Injectable } from '@angular/core';
import { Http, URLSearchParams, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { PositionTime } from '../models/postion-time.model';
import { Race } from '../models/race.model';
import { Position } from '../models/position.model';

import { RacesPositionTimes } from './race-position-times';

@Injectable()
export class TimesService {

  private racePositionTimes: RacesPositionTimes[] = new Array<RacesPositionTimes>();
  private observable: Observable<PositionTime[]>;

  constructor(private http: Http) {}

  public getTimesForPositionInRace(position: Position, race: Race): Observable<PositionTime[]> {
    if (position && race) {
      // check in aray
      for (let entry of this.racePositionTimes) {
        if (entry.position.id === position.id && entry.race.id === race.id) {
          return Observable.of(entry.times);
        }
      }
    }

    if (this.observable) {
      // request in progress
      return this.observable;

    } else {
      if (position && race) {
        return this.http.get('/rest/time/position/' + position.id + '/race/' + race.id)
          .map((response) => {
            let data = response.json();
            this.observable = null;
            let rpt: RacesPositionTimes = new RacesPositionTimes();
            rpt.position = position;
            rpt.race = race;
            rpt.times = data;
            this.racePositionTimes.push(rpt);
            return data;

          }).share();
      }
    }

    return this.observable;
  }

}
