import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';

import { Race } from '../races/race.model';

@Injectable()
export class RacesHttpService {
  protected endPoint: string = '/rest/races';

  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(protected http: HttpClient) { }

  public getRaces(): Observable<Race[]> {
    return this.http.get<Race[]>(this.endPoint, this.httpOptions);
   }
}
