import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { Data } from '../../model/data.model';

import { ClubsHttpService } from '../../clubs/clubs-http.service';

@Injectable()
export class ResultsHttpClubsService extends ClubsHttpService {

    constructor(protected http: HttpClient) {
        super(http);
    }

  public getClubsData(): Observable<Data> {
    return this.getData(this.endPoint);
   }
}
