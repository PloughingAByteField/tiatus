import { EventEmitter, Injectable } from '@angular/core';
import { Http, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Race } from '../models/race.model';

@Injectable()
export class RacesService {
  public searchEvent: EventEmitter<any> = new EventEmitter();

  private cachedData: Race[];
  private observable: Observable<Race[]>;

  constructor(private http: Http) {}

  public getRaceForId(id: number): Race {
    if (this.cachedData) {
      let races: Race[] = this.cachedData.filter((race: Race) => race.id === id);
      if (races) {
        return races[0];
      }
    }

    return null;
  }

  public getRaces(): Observable<Race[]> {
    if (this.cachedData) {
      // serve the cached data
      return Observable.of(this.cachedData);

   } else if (this.observable) {
     // request in progress
     return this.observable;

   } else {
    // fetch data -- share allows multiple subscribers
    this.observable = this.http.get('/rest/races')
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
