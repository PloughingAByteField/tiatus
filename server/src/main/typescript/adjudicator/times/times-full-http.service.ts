import { EventEmitter, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { EntryTime } from '../../times/entry-time.model';
import { Race } from '../../races/race.model';

import { TimesHttpService } from '../../times/times-http.service';
import { Data } from '../../model/data.model';

@Injectable()
export class TimesFullHttpService extends TimesHttpService {

  constructor(protected http: HttpClient) {
    super(http);
  }

  public getTimesForRace(race: Race): Observable<Data> {
    const url: string = this.timeEndPoint + race.id + '/full';
    return this.getData(url);
  }
}
