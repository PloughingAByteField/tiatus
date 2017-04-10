import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Disqualification, convertObjectToDisqualification } from './disqualification.model';

@Injectable()
export class DisqualificationHttpService {
  protected endpoint: string = '/rest/disqualifications';

  constructor(protected http: Http) {}

  public getDisqualifications(): Observable<Disqualification[]> {
    return this.http.get(this.endpoint)
      .map((response) => {
        return convertJsonToDisqualifications(response);
      }).share();
   }
}

function convertJsonToDisqualifications(response: Response): Disqualification[] {
    let jsonDisqualifications: Disqualification[] = response.json();
    let disqualifications: Disqualification[] = new Array<Disqualification>();
    jsonDisqualifications.map((json: Disqualification) => {
      disqualifications.push(convertObjectToDisqualification(json));
    });
    return disqualifications;
}
