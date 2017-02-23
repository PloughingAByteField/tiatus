import { EventEmitter, Injectable } from '@angular/core';
import { Headers, Http, URLSearchParams, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/toPromise';

import { PositionTime } from '../../models/postion-time.model';
import { Race } from '../../models/race.model';
import { Entry } from '../../models/entry.model';
import { Position } from '../../models/position.model';

import { RacePositionTimes } from '../models/race-position-times';

@Injectable()
export class TimesService {

  private racePositionTimes: RacePositionTimes[] = new Array<RacePositionTimes>();
  private observable: Observable<PositionTime[]>;
  private headers = new Headers({'Content-Type': 'application/json'});
  private timePositionEndPoint: string = '/rest/time/position/';
  private entryEndPoint: string = '/entry/';
  private raceEndPoint: string = '/race/';

  constructor(private http: Http) {}

  public setTimeForEntryAtPosition(
      entry: Entry, position: Position, positionTime: PositionTime): Promise<PositionTime> {
    return this.http
      .post(this.timePositionEndPoint + position.id + this.entryEndPoint + entry.id,
        JSON.stringify({time: positionTime.time}), {headers: this.headers})
      .toPromise()
      .then(() => {
        positionTime.synced = true;
        return positionTime;
      })
      .catch((err) => Promise.reject(err));
  }

  public updateTimeForEntryAtPosition(
      entry: Entry, position: Position, positionTime: PositionTime): Promise<PositionTime> {
    return this.http
      .put(this.timePositionEndPoint + position.id + this.entryEndPoint + entry.id,
        JSON.stringify({time: positionTime.time}), {headers: this.headers})
      .toPromise()
      .then(() => {
        positionTime.synced = true;
        return positionTime;
      })
      .catch((err) => Promise.reject(err));
  }

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
        return this.http.get(this.timePositionEndPoint + position.id + this.raceEndPoint + race.id)
          .map((response) => {
            let data = response.json();
            this.observable = null;
            let rpt: RacePositionTimes = new RacePositionTimes();
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
