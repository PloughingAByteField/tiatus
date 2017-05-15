import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Race, convertObjectToRace } from '../races/race.model';

@Injectable()
export class RacesHttpService {
   protected endpoint: string = '/rest/races';

  constructor(protected http: Http) {}

  public getRaces(): Observable<Race[]> {
    return this.http.get(this.endpoint)
      .map((response) => {
        if (response.status === 200) {
          return convertResponseToRaces(response);
        }
      }).share();
   }
}

export function convertResponseToRaces(response: Response): Race[] {
    const jsonRaces: Race[] = response.json();
    const races: Race[] = new Array<Race>();
    jsonRaces.map((json: Race) => {
      races.push(convertObjectToRace(json));
    });
    return races;
}
