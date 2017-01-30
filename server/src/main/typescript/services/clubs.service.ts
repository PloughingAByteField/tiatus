import { EventEmitter, Injectable } from '@angular/core';
import { Http, URLSearchParams, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Club } from '../models/club.model';

@Injectable()
export class ClubsService {
  public searchEvent: EventEmitter<any> = new EventEmitter();

  constructor(private http: Http) {}

  public getClubs(): Observable<Club[]> {
    return this.http.get('/rest/clubs')
      .map((response) => response.json());
  }

}
