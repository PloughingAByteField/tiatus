import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Race, convertObjectToRace } from '../../races/race.model';
import { Data } from '../model/data.model';

import { RacesHttpService, convertResponseToRaces }
    from '../../races/races-http.service';

@Injectable()
export class ResultsHttpRacesService extends RacesHttpService {

    private previousEtag: string;

    constructor(protected http: Http) {
        super(http);
    }

  public getRacesData(): Observable<Data> {
    return this.http.get(this.endpoint)
      .map((response: Response) => {
        if (response.status === 200) {
            let races: Race[] = convertResponseToRaces(response);
            let data: Data = new Data();
            data.data = races;
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
