import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Race } from '../../races/race.model';

import { RacesHttpService } from '../../races/races-http.service';

@Injectable()
export class AdjudicatorRacesHttpService extends RacesHttpService {
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(protected http: HttpClient) {
    super(http);
  }

  public updateRace(race: Race): Promise<Race> {
    return this.http
      .put(this.endPoint + '/' + race.id,
        JSON.stringify(race), {headers: this.headers})
      .toPromise()
      .then((res: Response) => {
        return race;
      })
      .catch((err) => Promise.reject(err));
  }

}
