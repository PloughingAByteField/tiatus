import {EventEmitter, Injectable} from '@angular/core';
import {Http, URLSearchParams} from '@angular/http';
import {Observable} from "rxjs/Observable";

export class Race {
  constructor(
    public name: string,
    public active: boolean,
    public closed: boolean,
    public drawLocked: boolean,
    public raceOrder: number,
    public startTime: string,
    public id: number
  ){}
}

@Injectable()
export class RacesService {
  searchEvent: EventEmitter<any> = new EventEmitter();

  constructor(private http: Http) {}

  getRaces(): Observable<Race[]> {
    return this.http.get('/rest/races')
      .map(response => response.json());
  }
}