import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';

import { Data } from '../../model/data.model';

import { EventsHttpService } from '../../events/events-http.service';

@Injectable()
export class ResultsHttpEventsService extends EventsHttpService {

    constructor(protected http: HttpClient) {
        super(http);
    }

  public getEventsData(): Observable<Data> {
    return this.getData(this.endPoint);
   }
}
