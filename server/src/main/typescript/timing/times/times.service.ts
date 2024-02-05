import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { PositionTime } from '../../times/postion-time.model';
import { Race } from '../../races/race.model';
import { Entry } from '../../entries/entry.model';
import { Position } from '../../positions/position.model';

import { TimesService } from '../../times/times.service';
import { TimingTimesHttpService } from './times-http.service';

@Injectable()
export class TimingTimesService extends TimesService {

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

  public getTimesForPositionInRace(position: Position, race: Race): Observable<PositionTime[]> {
    return this.service.getTimesForPositionInRace(position, race);
  }

}
