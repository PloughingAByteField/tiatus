import { EventEmitter, Injectable } from '@angular/core';
import { Http, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Disqualification } from '../models/disqualification.model';

@Injectable()
export class PenaltiesService {
  private cachedData: Disqualification[];

  private observable: Observable<Disqualification[]>;

  constructor(private http: Http) {}

  public getDisqualificationForEntry(id: number): Disqualification {
    if (this.cachedData) {
      return this.cachedData
        .filter((disqualification: Disqualification) => disqualification.entry === id).shift();
    }

    return null;
  }

  public getDisqualifications(): Observable<Disqualification[]> {
    if (this.cachedData) {
      // serve the cached data
      return Observable.of(this.cachedData);

   } else if (this.observable) {
     // request in progress
     return this.observable;

   } else {
    // fetch data -- share allows multiple subscribers
    this.observable = this.http.get('/rest/disqualifications')
      .map((response) => {
        // have data do not need observable
        this.cachedData = response.json();
        this.observable = null;
        return this.cachedData;

      }).share();

    return this.observable;
   }
  }
}
