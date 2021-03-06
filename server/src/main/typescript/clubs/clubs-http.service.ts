import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { Club } from './club.model';
import { CachedHttpService } from '../http/cached-http.service';
import { Data } from '../model/data.model';

@Injectable()
export class ClubsHttpService extends CachedHttpService {

  protected endPoint: string = '/rest/clubs';

  constructor(protected http: HttpClient) {
    super(http);
  }

  public getClubs(): Observable<Data> {
    return this.getData(this.endPoint);
  }
}
