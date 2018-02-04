import { EventEmitter, Injectable } from '@angular/core';
import { HttpHeaders, HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { EntryTime } from './entry-time.model';
import { PositionTime, convertJsonToPositionTime } from './postion-time.model';
import { Race } from '../races/race.model';
import { Entry } from '../entries/entry.model';

import { CachedHttpService } from '../http/cached-http.service';
import { Data } from '../model/data.model';

@Injectable()
export class TimesHttpService extends CachedHttpService {

  protected timeEndPoint: string = '/rest/time/race/';

  constructor(protected http: HttpClient) {
    super(http);
  }

  public getTimesForRace(race: Race): Observable<Data> {
    if (race) {
      const url: string = this.timeEndPoint + race.id;
      return this.getData(url);
    }
  }
}
