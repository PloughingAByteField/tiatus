import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';

import { Event } from '../events/event.model';

@Injectable()
export class UnassignedEventsHttpService {

  protected endPoint: string = '/rest/events/unassigned';

  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
  
  constructor(protected http: HttpClient) {}

  public getUnassignedEvents(): Observable<Event[]> {
    return this.http.get<Event[]>(this.endPoint, this.httpOptions);
   }
}
