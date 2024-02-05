import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';

import { RacesHttpService } from '../../races/races-http.service';

@Injectable()
export class ResultsHttpRacesService extends RacesHttpService {

    constructor(protected http: HttpClient) {
        super(http);
    }

}
