import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';


import { EventsHttpService } from '../../events/events-http.service';

@Injectable()
export class ResultsHttpEventsService extends EventsHttpService {

    constructor(protected http: HttpClient) {
        super(http);
    }
}
