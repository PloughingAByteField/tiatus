import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';

import { RaceEvent } from './race-event.model';

@Injectable()
export class RaceEventsHttpService {

  protected endPoint: string = '/rest/events/assigned';

  protected httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(protected http: HttpClient) {}

  public getRaceEvents(): Observable<RaceEvent[]> {
    return this.http.get<RaceEvent[]>(this.endPoint, this.httpOptions);
  }
}
