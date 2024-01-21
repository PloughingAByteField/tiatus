import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';

import { CachedHttpService } from '../http/cached-http.service';
import { Data } from '../model/data.model';

@Injectable()
export class RaceEventsHttpService extends CachedHttpService {

  protected endPoint: string = '/rest/events/assigned';

  constructor(protected http: HttpClient) {
    super(http);
  }

  public getRaceEvents(): Observable<Data> {
    return this.getData(this.endPoint);
  }
}
