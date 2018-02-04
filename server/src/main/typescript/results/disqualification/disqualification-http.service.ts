import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { Disqualification } from '../../disqualification/disqualification.model';
import { Data } from '../../model/data.model';

import { DisqualificationHttpService } from '../../disqualification/disqualification-http.service';

@Injectable()
export class ResultsHttpDisqualificationsService extends DisqualificationHttpService {

    constructor(protected http: HttpClient) {
        super(http);
    }

  public getDisqualificationsData(): Observable<Data> {
    return this.getData(this.endPoint);
   }
}
