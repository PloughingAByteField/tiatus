import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Penalty } from './penalty.model';

@Injectable()
export class PenaltiesHttpService {
  protected endPoint: string = '/rest/penalties';

  protected httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(protected http: HttpClient) {}

  public getPenalties(): Observable<Penalty[]> {
    return this.http.get<Penalty[]>(this.endPoint, this.httpOptions);
   }
}
