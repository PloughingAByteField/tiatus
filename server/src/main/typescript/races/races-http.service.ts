import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Race, convertJsonToRace } from '../races/race.model';

@Injectable()
export class RacesHttpService {
   protected endpoint: string = '/rest/races';

  constructor(protected http: Http) {}

  public getRaces(): Observable<Race[]> {
    return this.http.get(this.endpoint)
      .map((response) => {
        return convertJsonToRaces(response);
      }).share();
   }
}

function convertJsonToRaces(response: Response): Race[] {
    let jsonRaces: Race[] = response.json();
    let races: Race[] = new Array<Race>();
    jsonRaces.map((json: Race) => {
      races.push(convertJsonToRace(json));
    });
    return races;
}
