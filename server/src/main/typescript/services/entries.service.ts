import { EventEmitter, Injectable } from '@angular/core';
import { Http, URLSearchParams, Response } from '@angular/http';
import { Observable } from "rxjs/Observable";

import { Club } from './clubs.service';
import { Race } from './races.service';

export class Entry {
  constructor(
    public crew: string,
    public name: string,
    public number: number,
    public raceOrder: number,
    public timeOnly: boolean,
    public clubs: Club[],
    public race: Race,
    public raceName: string,
    public id: number
  ){
  }

  getClubNames(): string {
    return "Clubname";
  }
}

@Injectable()
export class EntriesService {
  searchEvent: EventEmitter<any> = new EventEmitter();

  constructor(private http: Http) {}

  getEntries(): Observable<Entry[]> {
    return this.http.get('/rest/entries')
      .map(response => response.json());
  }

}