import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Club } from './club.model';

@Injectable()
export class ClubsHttpService {

  protected endPoint: string = '/rest/clubs';

  protected httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(protected http: HttpClient) {}

  public getClubs(): Observable<Club[]> {
    return this.http.get<Club[]>(this.endPoint, this.httpOptions);
  }
}
