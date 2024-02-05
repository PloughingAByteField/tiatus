import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Disqualification } from './disqualification.model';

@Injectable()
export class DisqualificationHttpService {
  protected endPoint: string = '/rest/disqualifications';

  protected httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(protected http: HttpClient) {}

  public getDisqualifications(): Observable<Disqualification[]> {
    return this.http.get<Disqualification[]>(this.endPoint, this.httpOptions);
  }
}
