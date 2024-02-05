import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';

import { Position } from './position.model';

@Injectable()
export class PositionsHttpService {
  protected endPoint: string = '/rest/positions';

  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(protected http: HttpClient) { }

  public getPositions(): Observable<Position[]> {
    return this.http.get<Position[]>(this.endPoint, this.httpOptions);
  }
}
