import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Club, convertObjectToClub } from './club.model';

@Injectable()
export class ClubsHttpService {

  protected endPoint: string = '/rest/clubs';

  constructor(protected http: Http) {}

  public getClubs(): Observable<Club[]> {
    return this.http.get(this.endPoint)
      .map(convertResponseToClubs).share();
  }
}

function convertResponseToClubs(response: Response): Club[] {
    let jsonClubs: Club[] = response.json();
    let clubs: Club[] = new Array<Club>();
    jsonClubs.map((json: Club) => {
      clubs.push(convertObjectToClub(json));
    });
    return clubs;
}
