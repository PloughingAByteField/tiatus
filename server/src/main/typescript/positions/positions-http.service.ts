import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Position, convertObjectoPosition } from './position.model';

@Injectable()
export class PositionsHttpService {
  protected endpoint: string = '/rest/positions';

  constructor(protected http: Http) {}

  public getPositions(): Observable<Position[]> {
    return this.http.get(this.endpoint)
      .map(convertResponseToPositions).share();
  }
}

export function convertResponseToPositions(response: Response): Position[] {
    let jsonPositions: Position[] = response.json();
    let positions: Position[] = new Array<Position>();
    jsonPositions.map((json: Position) => {
      positions.push(convertObjectoPosition(json));
    });
    return positions;
}
