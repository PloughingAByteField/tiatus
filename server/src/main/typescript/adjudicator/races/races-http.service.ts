import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Race } from '../../races/race.model';

import { RacesHttpService } from '../../races/races-http.service';

@Injectable()
export class AdjudicatorRacesHttpService extends RacesHttpService {
  private headers = new Headers({'Content-Type': 'application/json'});

  constructor(protected http: Http) {
    super(http);
  }

  public updateRace(race: Race): Promise<Race> {
    return this.http
      .put(this.endpoint + '/' + race.id,
        JSON.stringify(race), {headers: this.headers})
      .toPromise()
      .then((res: Response) => {
        return race;
      })
      .catch((err) => Promise.reject(err));
  }

}
