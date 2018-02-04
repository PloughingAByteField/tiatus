import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';

import { Race } from '../races/race.model';
import { CachedHttpService } from '../http/cached-http.service';
import { Data } from '../model/data.model';

@Injectable()
export class RacesHttpService extends CachedHttpService {
   protected endPoint: string = '/rest/races';

  constructor(protected http: HttpClient) {
    super(http);
  }

  public getRaces(): Observable<Data> {
    return this.getData(this.endPoint);
   }
}
