import { EventEmitter, Injectable } from '@angular/core';
import { Headers, Http, URLSearchParams, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/toPromise';

import { PositionTime, convertJsonToPositionTime } from '../../times/postion-time.model';
import { Race } from '../../races/race.model';
import { Entry } from '../../entries/entry.model';
import { EntryTime } from '../../times/entry-time.model';
import { Position } from '../../positions/position.model';

import { TimesHttpService } from '../../times/times-http.service';

@Injectable()
export class TimingTimesHttpService extends TimesHttpService {

  protected timeEndPoint: string = '/rest/time/race/';

  private headers = new Headers({'Content-Type': 'application/json'});
  private timePositionEndPoint: string = '/rest/time/position/';
  private entryEndPoint: string = '/entry/';
  private raceEndPoint: string = '/race/';

  constructor(protected http: Http) {
    super(http);
  }

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

  public getTimesForPositionInRace(position: Position, race: Race): Observable<EntryTime[]> {
    let url: string = this.timePositionEndPoint + position.id + this.raceEndPoint + race.id;
    return this.getTimesForRaceWithUrl(race, url);
  }
}
