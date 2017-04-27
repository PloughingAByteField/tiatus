import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Penalty, convertObjectToPenalty } from '../../penalties/penalty.model';
import { Data } from '../model/data.model';

import { PenaltiesHttpService, convertResponseToPenalties } from '../../penalties/penalties-http.service';

@Injectable()
export class ResultsHttpPenaltiesService extends PenaltiesHttpService {

    private previousEtag: string;

    constructor(protected http: Http) {
        super(http);
    }

  public getPenaltiesData(): Observable<Data> {
    return this.http.get(this.endpoint)
      .map((response: Response) => {
        if (response.status === 200) {
            const penalties: Penalty[] = convertResponseToPenalties(response);
            const data: Data = new Data();
            data.data = penalties;
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
