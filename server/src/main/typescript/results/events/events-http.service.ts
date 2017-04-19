import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Event } from '../../events/event.model';
import { Data } from '../model/data.model';

import { EventsHttpService, convertResponseToEvents }
    from '../../events/events-http.service';

@Injectable()
export class ResultsHttpEventsService extends EventsHttpService {

    private previousEtag: string;

    constructor(protected http: Http) {
        super(http);
    }

  public getEventsData(): Observable<Data> {
    return this.http.get(this.endpoint)
      .map((response: Response) => {
        if (response.status === 200) {
            let events: Event[] = convertResponseToEvents(response);
            let data: Data = new Data();
            data.data = events;
            data.cached = false;
            let currentEtag: string = response.headers.get('etag');
            if (this.previousEtag) {
                if (this.previousEtag === currentEtag) {
                    data.cached = true;
                } else {
                    this.previousEtag = currentEtag;
                }
            } else {
                this.previousEtag = currentEtag;
            }
            return data;
        }
      })
      .share();
   }
}
