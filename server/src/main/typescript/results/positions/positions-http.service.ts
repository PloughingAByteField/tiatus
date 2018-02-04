import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';

import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';

import { Data } from '../../model/data.model';

import { PositionsHttpService } from '../../positions/positions-http.service';

@Injectable()
export class ResultsHttpPositionsService extends PositionsHttpService {

    constructor(protected http: HttpClient) {
        super(http);
    }

  public getPositionsData(): Observable<Data> {
    return this.getData(this.endPoint);
   }
}
