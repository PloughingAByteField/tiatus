import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Position } from '../../positions/position.model';
import { Data } from '../model/data.model';

import { PositionsHttpService, convertResponseToPositions }
    from '../../positions/positions-http.service';

@Injectable()
export class ResultsHttpPositionsService extends PositionsHttpService {

    private previousEtag: string;

    constructor(protected http: Http) {
        super(http);
    }

  public getPositionsData(): Observable<Data> {
    return this.http.get(this.endpoint)
      .map((response: Response) => {
        if (response.status === 200) {
            let positions: Position[] = convertResponseToPositions(response);
            let data: Data = new Data();
            data.data = positions;
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
