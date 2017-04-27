import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Penalty, convertObjectToPenalty } from './penalty.model';

@Injectable()
export class PenaltiesHttpService {
  protected endpoint: string = '/rest/penalties';

  constructor(protected http: Http) {}

  public getPenalties(): Observable<Penalty[]> {
    return this.http.get(this.endpoint)
      .map(convertResponseToPenalties).share();
   }
}

export function convertResponseToPenalties(response: Response): Penalty[] {
    const jsonPenalties: Penalty[] = response.json();
    const penalties: Penalty[] = new Array<Penalty>();
    jsonPenalties.map((json: Penalty) => {
      penalties.push(convertObjectToPenalty(json));
    });
    return penalties;
}
