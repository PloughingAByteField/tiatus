import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Club } from '../../clubs/club.model';
import { Data } from '../model/data.model';

import { ClubsHttpService, convertResponseToClubs } from '../../clubs/clubs-http.service';

@Injectable()
export class ResultsHttpClubsService extends ClubsHttpService {

    private previousEtag: string;

    constructor(protected http: Http) {
        super(http);
    }

  public getClubsData(): Observable<Data> {
    return this.http.get(this.endPoint)
      .map((response: Response) => {
        if (response.status === 200) {
            const clubs: Club[] = convertResponseToClubs(response);
            const data: Data = new Data();
            data.data = clubs;
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
