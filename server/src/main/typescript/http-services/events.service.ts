import { EventEmitter, Injectable } from '@angular/core';
import { Http, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Event } from '../models/event.model';

@Injectable()
export class EventsService {
  public searchEvent: EventEmitter<any> = new EventEmitter();

  constructor(private http: Http) {}

}
