import { EventEmitter, Injectable } from '@angular/core';
import { Http, URLSearchParams, Response } from '@angular/http';
import { Observable } from "rxjs/Observable";

export class Club {
  constructor(
    public name: string,
    public id: number
  ){}
}

@Injectable()
export class ClubsService {
  searchEvent: EventEmitter<any> = new EventEmitter();

  constructor(private http: Http) {}

  getClubs(): Observable<Club[]> {
    return this.http.get('/rest/clubs')
      .map(response => response.json());
  }

}
