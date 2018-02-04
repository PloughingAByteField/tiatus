import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';

import { Penalty, convertObjectToPenalty } from '../../penalties/penalty.model';
import { Data } from '../../model/data.model';

import { PenaltiesHttpService } from '../../penalties/penalties-http.service';

@Injectable()
export class ResultsHttpPenaltiesService extends PenaltiesHttpService {

    constructor(protected http: HttpClient) {
        super(http);
    }

  public getPenaltiesData(): Observable<Data> {
    return this.getData(this.endPoint);
   }
}
