import { EventEmitter, Injectable } from '@angular/core';
import { Http, URLSearchParams, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Position } from '../models/position.model';

@Injectable()
export class PositionsService {
  public searchEvent: EventEmitter<any> = new EventEmitter();

  constructor(private http: Http) {}

  public getPositions(): Observable<Position[]> {
    return this.http.get('/rest/positions')
      .map((response) => response.json()).share();
  }

}
