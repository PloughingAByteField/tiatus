import { EventEmitter, Injectable } from '@angular/core';
import { Headers, Http, URLSearchParams, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { EntryTime } from '../../times/entry-time.model';
import { Race } from '../../races/race.model';

import { TimesHttpService } from '../../times/times-http.service';

@Injectable()
export class TimesFullHttpService extends TimesHttpService {

  constructor(protected http: Http) {
    super(http);
  }

  public getTimesForRace(race: Race): Observable<EntryTime[]> {
    let url: string = this.timeEndPoint + race.id + '/full';
    return this.getTimesForRaceWithUrl(race, url);
  }
}
