import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';

import { Observable } from 'rxjs';
import { BehaviorSubject } from 'rxjs';

import { Event } from '../events/event.model';
import { CachedHttpService } from '../http/cached-http.service';
import { Data } from '../model/data.model';

@Injectable()
export class UnassignedEventsHttpService extends CachedHttpService {

  protected endPoint: string = '/rest/events/unassigned';

  constructor(protected http: HttpClient) {
    super(http);
  }

  public getUnassignedEvents(): Observable<Data> {
    return this.getData(this.endPoint);
   }
}
