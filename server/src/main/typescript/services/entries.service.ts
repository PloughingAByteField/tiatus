import { EventEmitter, Injectable } from '@angular/core';
import { Http, URLSearchParams, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Entry } from '../models/entry.model';

@Injectable()
export class EntriesService {
  searchEvent: EventEmitter<any> = new EventEmitter();

  constructor(private http: Http) {}

  getEntries(): Observable<Entry[]> {
    return this.http.get('/rest/entries')
      .map(response => response.json());
  }

}