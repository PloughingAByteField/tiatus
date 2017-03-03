import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Position } from '../models/position.model';

@Injectable()
export class PositionsService {
  constructor(private http: Http) {}

  public getPositions(): Observable<Position[]> {
    return this.http.get('/rest/positions')
      .map((response: Response) => response.json()).share();
  }

}
