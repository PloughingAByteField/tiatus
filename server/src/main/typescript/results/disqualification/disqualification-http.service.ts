import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Disqualification } from '../../disqualification/disqualification.model';
import { Data } from '../model/data.model';

import { DisqualificationHttpService, convertResponseToDisqualifications } from '../../disqualification/disqualification-http.service';

@Injectable()
export class ResultsHttpDisqualificationsService extends DisqualificationHttpService {

    private previousEtag: string;

    constructor(protected http: Http) {
        super(http);
    }

  public getDisqualificationsData(): Observable<Data> {
    return this.http.get(this.endpoint)
      .map((response: Response) => {
        if (response.status === 200) {
            const disqualifications: Disqualification[]
                = convertResponseToDisqualifications(response);
            const data: Data = new Data();
            data.data = disqualifications;
            data.cached = false;
            const currentEtag: string = response.headers.get('etag');
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
