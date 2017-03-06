import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Club, convertJsonToClub } from './club.model';

@Injectable()
export class ClubsHttpService {

  protected endPoint: string = '/rest/clubs';

  constructor(protected http: Http) {}

  public getClubs(): Observable<Club[]> {
    return this.http.get(this.endPoint)
      .map(convertJsonToClubs).share();
  }
}

function convertJsonToClubs(response: Response): Club[] {
    let jsonClubs: Club[] = response.json();
    let clubs: Club[] = new Array<Club>();
    jsonClubs.map((json: Club) => {
      clubs.push(convertJsonToClub(json));
    });
    return clubs;
}
