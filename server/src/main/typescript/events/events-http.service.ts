import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Event } from './event.model';

@Injectable()
export class EventsHttpService {

  protected endPoint: string = '/rest/events';

  protected httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(protected http: HttpClient) {}

  public getEvents(): Observable<Event[]> {
    return this.http.get<Event[]>(this.endPoint, this.httpOptions);
   }
}
