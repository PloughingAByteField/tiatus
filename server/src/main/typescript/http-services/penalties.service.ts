import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Penalty, convertJsonToPenalty } from '../models/penalty.model';

@Injectable()
export class PenaltiesService {
  protected endpoint: string = '/rest/penalties';

  private cachedData: Penalty[];

  private observable: Observable<Penalty[]>;

  constructor(protected http: Http) {}

  public getPenaltyForEntryId(id: number): Penalty {
    if (this.cachedData) {
      return this.cachedData
        .filter((penalty: Penalty) => penalty.entry === id).shift();
    }

    return null;
  }

  public getPenalties(): Observable<Penalty[]> {
    if (this.cachedData) {
      // serve the cached data
      return Observable.of(this.cachedData);

   } else if (this.observable) {
     // request in progress
     return this.observable;

   } else {
    // fetch data -- share allows multiple subscribers
    this.observable = this.http.get(this.endpoint)
      .map((response) => {
        // have data do not need observable
        this.cachedData = convertJsonToPenalties(response);
        this.observable = null;
        return this.cachedData;

      }).share();

    return this.observable;
   }
  }
}

function convertJsonToPenalties(response: Response): Penalty[] {
    let jsonPenalties: Penalty[] = response.json();
    let penalties: Penalty[] = new Array<Penalty>();
    jsonPenalties.map((json: Penalty) => {
      penalties.push(convertJsonToPenalty(json));
    });
    return penalties;
}
