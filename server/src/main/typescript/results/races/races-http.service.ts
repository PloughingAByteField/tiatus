import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';

import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';

import { Race, convertObjectToRace } from '../../races/race.model';
import { Data } from '../../model/data.model';

import { RacesHttpService } from '../../races/races-http.service';

@Injectable()
export class ResultsHttpRacesService extends RacesHttpService {

    constructor(protected http: HttpClient) {
        super(http);
    }

  public getRacesData(): Observable<Data> {
      return this.getData(this.endPoint);
   }
}
