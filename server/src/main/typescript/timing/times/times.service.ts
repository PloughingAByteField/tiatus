import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { PositionTime, convertJsonToPositionTime } from '../../times/postion-time.model';
import { Race } from '../../races/race.model';
import { Entry } from '../../entries/entry.model';
import { EntryTime } from '../../times/entry-time.model';
import { Position } from '../../positions/position.model';

import { RacePositionTimes } from './race-position-times';

import { TimesService } from '../../times/times.service';
import { TimingTimesHttpService } from './times-http.service';
import { Data } from '../../model/data.model';

@Injectable()
export class TimingTimesService extends TimesService {

  private racePositionTimes: RacePositionTimes[] = new Array<RacePositionTimes>();
  private observable: Observable<PositionTime[]>;
  private headers = new HttpHeaders({'Content-Type': 'application/json'});
  private timePositionEndPoint: string = '/rest/time/position/';
  private entryEndPoint: string = '/entry/';
  private raceEndPoint: string = '/race/';

  constructor(protected service: TimingTimesHttpService) {
    super(service);
  }

 public setTimeForEntryAtPosition(
      entry: Entry, position: Position, positionTime: PositionTime): Promise<PositionTime> {
        return this.service.setTimeForEntryAtPosition(entry, position, positionTime);
  }

  public updateTimeForEntryAtPosition(
      entry: Entry, position: Position, positionTime: PositionTime): Promise<PositionTime> {
      return this.service.updateTimeForEntryAtPosition(entry, position, positionTime);
  }

  public getTimesForPositionInRace(position: Position, race: Race): Observable<Data> {
    return this.service.getTimesForPositionInRace(position, race);
  }

}
