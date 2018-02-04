import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { Position } from './position.model';

import { CachedHttpService } from '../http/cached-http.service';
import { Data } from '../model/data.model';

@Injectable()
export class PositionsHttpService extends CachedHttpService {
  protected endPoint: string = '/rest/positions';

  constructor(protected http: HttpClient) {
    super(http);
  }

  public getPositions(): Observable<Data> {
    return this.getData(this.endPoint);
  }
}
