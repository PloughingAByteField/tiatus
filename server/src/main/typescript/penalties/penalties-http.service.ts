import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Penalty } from './penalty.model';
import { CachedHttpService } from '../http/cached-http.service';
import { Data } from '../model/data.model';

@Injectable()
export class PenaltiesHttpService extends CachedHttpService {
  protected endPoint: string = '/rest/penalties';

  constructor(protected http: HttpClient) {
    super(http);
  }

  public getPenalties(): Observable<Data> {
    return this.getData(this.endPoint);
   }
}
